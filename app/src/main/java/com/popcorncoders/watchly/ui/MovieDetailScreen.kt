package com.popcorncoders.watchly.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import com.popcorncoders.watchly.model.Movie
import com.popcorncoders.watchly.viewmodel.MovieDetailViewModel

@Composable
fun MovieDetailScreen(
    movie: Movie?,
    viewModel: MovieDetailViewModel,
    onBackClick: () -> Unit
) {
    Scaffold { innerPadding ->
        if (movie == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
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

                Text("Your Rating: $rating")

                Text(
                    text = if (isFavorite) " Saved to Favorite" else "Not saved",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                StarRating(
                    rating = rating,
                    onRatingChanged = { newRating ->
                        viewModel.updateRating(newRating,movie)

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
    Row{
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating)
                    Icons.Filled.Star
                else
                    Icons.Outlined.StarBorder,
                contentDescription = "Star $i",
                tint = if (i <= rating)
                    MaterialTheme.colorScheme.primary
                 else
                     MaterialTheme.colorScheme.outline,

                modifier = Modifier
                    .size(36.dp)
                    .clickable { onRatingChanged(i)}
            )
        }
    }
}