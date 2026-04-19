package com.popcorncoders.watchly.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.popcorncoders.watchly.model.Movie
import com.popcorncoders.watchly.viewmodel.MovieDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movie: Movie?,
    viewModel: MovieDetailViewModel,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onFavoritesPageClick: () -> Unit,
    onRatedMoviesPageClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(movie?.title ?: "Movie Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onToggleDarkMode) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.Brightness7 else Icons.Default.Brightness4,
                            contentDescription = "Toggle dark mode"
                        )
                    }

                    IconButton(onClick = onRatedMoviesPageClick) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rated movies"
                        )
                    }

                    IconButton(onClick = onFavoritesPageClick) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites page"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (movie == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Movie not found",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onBackClick) {
                    Text("Go Back")
                }
            }
        } else {
            val rating by viewModel.rating.collectAsState()
            val isFavorite by viewModel.isFavorite.collectAsState()

            LaunchedEffect(movie.id) {
                viewModel.loadFavorite(movie.id)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { viewModel.toggleFavorite(movie) }) {
                    Row {
                        Icon(
                            imageVector = if (isFavorite) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            contentDescription = "Toggle favorite"
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        Text(if (isFavorite) "Remove from Favorites" else "Add to Favorites")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Your Rating: $rating / 5",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                StarRating(
                    rating = rating,
                    onRatingChanged = { newRating ->
                        viewModel.updateRating(newRating, movie)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = onBackClick) {
                    Text("Back")
                }
            }
        }
    }
}

@Composable
fun StarRating(
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) {
                    Icons.Filled.Star
                } else {
                    Icons.Outlined.StarBorder
                },
                contentDescription = "Star $i",
                tint = if (i <= rating) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                },
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clickable { onRatingChanged(i) }
            )
        }
    }
}