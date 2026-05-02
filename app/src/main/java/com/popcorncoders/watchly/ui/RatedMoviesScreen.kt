package com.popcorncoders.watchly.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.popcorncoders.watchly.R
import com.popcorncoders.watchly.data.local.entity.RatingEntity
import com.popcorncoders.watchly.ui.theme.activeHomeColor
import com.popcorncoders.watchly.ui.theme.activeRatedColor
import com.popcorncoders.watchly.ui.theme.activeFavoriteColor
import com.popcorncoders.watchly.ui.theme.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatedMoviesScreen(
    ratedMovies: List<RatingEntity>,
    currentScreen: Screen,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onHomeClick: () -> Unit,
    onRatedMoviesPageClick: () -> Unit,
    onFavoritesPageClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onRemoveRating : (Int) -> Unit,
    onBackClick: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Ratings",
                        fontWeight = FontWeight.Bold
                    ) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onHomeClick,
                        enabled = currentScreen != Screen.HOME,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
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
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rated movies",
                            modifier = Modifier.size(22.dp),
                            tint = if (currentScreen == Screen.RATED)
                                activeRatedColor
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = onFavoritesPageClick,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites page",
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
                            contentDescription = "Toggle dark mode",
                            tint = MaterialTheme.colorScheme.onSurface,
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
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                ratedMovies.isEmpty() -> {
                    Text(
                        text = "No rated movies yet.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        items(ratedMovies) { movie ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                                    .clickable { onMovieClick(movie.movieId)}
                            ) {
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

                                    // Title + starts + rating
                                    Column(modifier = Modifier.weight(1f)) {
                                        // Movie title
                                            Text(
                                                text = movie.title,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold
                                            )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        // Gold stars
                                        Row {
                                            repeat(5) { i ->
                                                Icon(
                                                    imageVector = if (i < movie.rating) Icons.Filled.Star else
                                                        Icons.Outlined.StarBorder,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier
                                                            .size(16.dp)
                                                            .padding(end = 2.dp)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            // Number rating
                                            Text(
                                                "Rating: ${movie.rating}/5",
                                                style = MaterialTheme.typography.bodyMedium
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))

                                            // Tap to view more
                                            Text(
                                                text = "Tap to view more",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }

                                        // X button
                                        IconButton(
                                            onClick = { onRemoveRating(movie.movieId)},
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove rating",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}