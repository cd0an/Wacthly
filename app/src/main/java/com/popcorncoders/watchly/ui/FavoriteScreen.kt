package com.popcorncoders.watchly.ui

import com.popcorncoders.watchly.data.local.entity.FavoriteEntity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    favorites: List<FavoriteEntity>,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onRatedMoviesPageClick: () -> Unit,
    onRemoveClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Favorites") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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

                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (favorites.isEmpty()) {
                Text("No favorite movies yet.")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(favorites) { movie ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    movie.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        onRemoveClick(movie.movieId)
                                    }
                                ) {
                                    Text("Remove from Favorites")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}