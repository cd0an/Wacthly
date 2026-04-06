package com.popcorncoders.watchly.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.popcorncoders.watchly.data.local.entity.FavoriteEntity
import com.popcorncoders.watchly.viewmodel.MovieDetailViewModel


// It draws UI on the screen using Compose and uses some new Material3 features
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: Int,                       // ID of the movie being displayed
    title: String,                      // Title of the movie
    overview: String,                   // Description of the movie
    posterPath: String,                 // Poster image path of the movie
    viewModel: MovieDetailViewModel,    // Handle logic like favorites
    onBackClick: () -> Unit             // called function when needed
) {
    // Observe whether the movie is currently in favorites
    val isFavorite by viewModel.isFavorite.collectAsState()

    // Check if this movie is already a favorite when screen loads
    LaunchedEffect(movieId) {
        viewModel.checkIfFavorite(movieId)
    }

    // It gives a basic screen layout with top bar and content area
    Scaffold(
        topBar = {
            // It shows a bar at the top with the movie title
            TopAppBar(
                title = { Text(title) }
            )
        }
    ) { padding ->    // It is provided to avoid overlapping system UI like status bar

        // It arranges elements vertically (from top to bottom)
        Column(
            modifier = Modifier
                .fillMaxSize()                           // Take up the whole screen
                .padding(padding)         // Add Scaffold padding
                .padding(16.dp)                    // Add extra space inside the content
        ) {

            // Placeholder for movie poster inside a Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                // stacking content and centering
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Display placeholder text while image is not loaded
                    Text("Poster")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))  // Add vertical space

            // Display movie title
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))   // Small space between title and overview

            // Display movie description
            Text(
                text = overview,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Add or remove from favorites
            Button(
                onClick = {

                    // Create a FavoriteEntity representing this movie
                    val favorite = FavoriteEntity(
                        movieId = movieId,
                        title = title,
                        overview = overview,
                        posterPath = posterPath
                    )
                    // Toggle favorite state in ViewModel
                    viewModel.toggleFavorite(favorite)
                },
                modifier = Modifier.fillMaxWidth()    // Button fills the width of screen
            ) {
                // Button text changes depending on favorite state
                Text(if (isFavorite) "Remove from Favorites" else "Add to Favorites")
            }
        }
    }
}