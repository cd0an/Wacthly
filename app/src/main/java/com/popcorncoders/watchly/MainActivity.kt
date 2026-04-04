package com.popcorncoders.watchly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.padding
import com.popcorncoders.watchly.ui.theme.WatchlyTheme
import com.popcorncoders.watchly.viewmodel.MovieListViewModel

// Main entry point of the app
class MainActivity : ComponentActivity() {

    // Creates an instance of the ViewModel
    // Holds and manages UI-related data
    private val movieListViewModel: MovieListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Jetpack Compose UI starts
        setContent {
            // Applies the app's theme
            WatchlyTheme {
                // This provides basic layout structure & handles things like padding for system UI
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Calls MovieList screen and passes:
                    // 1. ViewModel (data source)
                    // 2. Padding so content doesn't overlap system UI
                    MovieListScreen(
                        viewModel = movieListViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel,
    modifier: Modifier = Modifier
) {
    // Observe the LiveData from ViewModel
    // When data changes, UI auto updates
    val movies = viewModel.movies.observeAsState(emptyList())

    // Scrollable list
    LazyColumn {
        // Loop through each movie in the list
        items(movies.value) { movie ->
            // Display each movie title as text
            Text(
                text = movie.title, // Comes from API
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


