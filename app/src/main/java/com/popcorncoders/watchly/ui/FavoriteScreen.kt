package com.popcorncoders.watchly.ui

import com.popcorncoders.watchly.data.local.entity.FavoriteEntity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.alpha
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.popcorncoders.watchly.R
import com.popcorncoders.watchly.ui.theme.activeHomeColor
import com.popcorncoders.watchly.ui.theme.activeRatedColor
import com.popcorncoders.watchly.ui.theme.activeFavoriteColor
import com.popcorncoders.watchly.ui.theme.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    favorites: List<FavoriteEntity>,
    isLoading: Boolean,
    errorMessage: String?,
    currentScreen: Screen,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onHomeClick: () -> Unit,
    onRatedMoviesPageClick: () -> Unit,
    onRemoveClick: (Int) -> Unit,
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {

    val listState = rememberLazyListState()
    val density = LocalDensity.current

    val alpha by animateFloatAsState(
        targetValue = if (listState.isScrollInProgress) 1f else 0.4f,
        label = ""
    )

    val layoutInfo by remember { derivedStateOf { listState.layoutInfo } }
    val totalItems = favorites.size
    val visibleItems by remember { derivedStateOf { layoutInfo.visibleItemsInfo.size } }

    val viewportHeightPx by remember {
        derivedStateOf {
            layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
        }
    }

    val scrollProgress by remember {
        derivedStateOf {
            val maxScrollIndex = (totalItems - visibleItems).coerceAtLeast(1)
            (listState.firstVisibleItemIndex.toFloat() / maxScrollIndex)
                .coerceIn(0f, 1f)
        }
    }

    val thumbHeightRatio by remember {
        derivedStateOf {
            if (totalItems == 0) 1f
            else visibleItems.toFloat() / totalItems.toFloat()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Favorites",
                        fontWeight = FontWeight.Bold
                    ) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = onHomeClick,
                        enabled = currentScreen != Screen.HOME,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            Icons.Default.Home,
                            "Home",
                            modifier = Modifier.size(22.dp),
                            tint = if (currentScreen == Screen.HOME)
                                activeHomeColor
                            else
                                MaterialTheme.colorScheme.onSurface
                            )

                    }

                    IconButton(
                        onClick = onRatedMoviesPageClick,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            "Rated",
                            modifier = Modifier.size(22.dp),
                            tint = if (currentScreen == Screen.RATED)
                                activeRatedColor
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.size(30.dp),
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            "Favorites page",
                            modifier = Modifier.size(22.dp),
                            tint = if (currentScreen == Screen.FAVORITES)
                                activeFavoriteColor
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = onToggleDarkMode,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = if (isDarkMode)
                                Icons.Default.Brightness7
                            else
                                Icons.Default.Brightness4,
                            contentDescription = "Theme",
                            modifier = Modifier.size(22.dp)
                        )
                    }

                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(errorMessage, color = MaterialTheme.colorScheme.error)
                    }
                }

                favorites.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No favorite movies yet.")
                    }
                }

                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(favorites) { movie ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onMovieClick(movie.movieId) }
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Tap to view more + X button to remove
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        // Movie Poster
                                        AsyncImage(
                                            model = "https://image.tmdb.org/t/p/w500${movie.posterPath ?: ""}",
                                            contentDescription = "Poster for ${movie.title}",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .height(80.dp)
                                                .width(56.dp)
                                                .clip(MaterialTheme.shapes.medium),
                                            error = painterResource(id = R.drawable.ic_launcher_foreground),
                                            placeholder = painterResource(id = R.drawable.ic_launcher_foreground)
                                        )

                                        Spacer(modifier = Modifier.width(12.dp))

                                        // Title + tap to view more
                                        Column(modifier = Modifier.weight(1f)) {
                                            // Title
                                            Text(
                                                movie.title,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold
                                            )

                                            // Tap to view more
                                            Text(
                                                text = "Tap to view more",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }

                                        // X button
                                        IconButton(
                                            onClick = { onRemoveClick(movie.movieId) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove favorite",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (totalItems > visibleItems) {
                        val thumbHeightPx = viewportHeightPx * thumbHeightRatio
                        val thumbOffsetPx = viewportHeightPx * scrollProgress

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 4.dp)
                                .fillMaxHeight()
                                .width(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Color.Gray.copy(alpha = 0.2f),
                                        shape = MaterialTheme.shapes.small
                                    )
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(with(density) { thumbHeightPx.toDp() })
                                    .offset(y = with(density) { thumbOffsetPx.toDp() })
                                    .background(
                                        if (isDarkMode)
                                            Color.White.copy(alpha = 0.7f)
                                        else
                                            Color.Black.copy(alpha = 0.5f)
                                    )
                                    .alpha(alpha)
                            )
                        }
                    }
                }
            }
        }
    }
}