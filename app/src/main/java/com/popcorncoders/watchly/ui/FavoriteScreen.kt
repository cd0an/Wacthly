package com.popcorncoders.watchly.ui

import com.popcorncoders.watchly.data.local.entity.FavoriteEntity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder

@Composable
fun FavoritesScreen(
    favorites: List<FavoriteEntity>,
    onRemoveClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                "Favorites",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (favorites.isEmpty()) {
                Text("No favorites yet.")
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
                                Row {
                                    for (i in 1..5) {
                                        Icon(
                                            imageVector = if (i <= movie.rating)
                                                Icons.Filled.Star
                                            else
                                                Icons.Outlined.StarBorder,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)

                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text("${movie.rating}/5")
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        onRemoveClick(movie.movieId)
                                    }
                                ) {
                                    Text("Remove")
                                }
                            }
                        }

                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onBackClick) {
                Text("Back")
            }
        }
    }
}