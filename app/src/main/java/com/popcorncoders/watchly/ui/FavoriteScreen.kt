package com.popcorncoders.watchly.ui

import androidx.compose.foundation.layout.*           // For Box, Column, Spacer, padding, fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn  // For vertical scrollable lists
import androidx.compose.foundation.lazy.items       // To loop through a list in LazyColumn
import androidx.compose.material3.*                // Material Design components like Card, Text, Scaffold
import androidx.compose.runtime.*                  // For @Composable, state handling
import androidx.compose.ui.Modifier               // Modify size, padding, alignment of UI elements
import androidx.compose.ui.unit.dp                 // dp = density-independent pixels
import com.popcorncoders.watchly.data.local.entity.FavoriteEntity


// It draws UI on the screen using Compose and uses some new Material3 features
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    favorites: List<FavoriteEntity>,            // List of favorite movies to show
    onMovieClick: (FavoriteEntity) -> Unit     // called whenever a favorite movie is selected
) {

    // It gives a basic screen layout: top bar, content area, etc.
    Scaffold(
        topBar = {
            // This is the top bar with a title centered
            CenterAlignedTopAppBar(
                title = { Text("Favorites") }   // The title at the top of screen
            )
        }
    ) { padding ->  // Padding is added to avoid overlapping system bars

        // If the user has no favorites, show a message
        if (favorites.isEmpty()) {
            // Box lets put something in the center of the screen
            Box(
                modifier = Modifier
                    .fillMaxSize()             // Take up the full screen
                    .padding(padding),         // keep space for top bar /system UI
                contentAlignment = androidx.compose.ui.Alignment.Center // Center the message
            ) {
                Text("No favorites yet")
            }
        } else {
            // If there are favorites, show them in a scrollable list
            LazyColumn(
                modifier = Modifier.padding(padding), // keep space for system bars
                contentPadding = PaddingValues(16.dp), // Add some space around the list
                verticalArrangement = Arrangement.spacedBy(12.dp) // Space between each item
            ) {
                // Loop through all favorites movies
                items(favorites) { movie ->

                    // Each favorite movie is displayed inside a Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()        // Card fills the width of the parent
                            .padding(4.dp),        // Small padding around each card
                        onClick = { onMovieClick(movie) } // do something when clicked
                    ) {
                        // It puts items vertically inside the card
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Movie title text
                            Text(
                                movie.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(4.dp)) // Small space between title and overview

                            // Show the movie overview (description), max 2 lines.
                            Text(
                                movie.overview,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}