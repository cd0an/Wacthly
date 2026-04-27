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
import androidx.compose.ui.unit.dp
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
    onBackClick: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Rated Movies") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onHomeClick) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = if (currentScreen == Screen.HOME)
                                activeHomeColor
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = onRatedMoviesPageClick) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rated movies",
                            tint = if (currentScreen == Screen.RATED)
                                activeRatedColor
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = onFavoritesPageClick) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites page",
                            tint = if (currentScreen == Screen.FAVORITES)
                                activeFavoriteColor
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = onToggleDarkMode) {
                        Icon(
                            imageVector = if (isDarkMode)
                                Icons.Default.Brightness7
                            else
                                Icons.Default.Brightness4,
                            contentDescription = "Toggle dark mode",
                            tint = MaterialTheme.colorScheme.onSurface
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
                            ) {
                                Column(
                                    modifier = Modifier
                                        .clickable { onMovieClick(movie.movieId) }
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = movie.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row {
                                        repeat(5) { i ->
                                            Icon(
                                                imageVector =
                                                    if (i < movie.rating)
                                                        Icons.Filled.Star
                                                    else
                                                        Icons.Outlined.StarBorder,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(end = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text("Rating: ${movie.rating}/5")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}