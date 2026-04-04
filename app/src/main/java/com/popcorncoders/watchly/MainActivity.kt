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
import com.popcorncoders.watchly.ui.theme.WatchlyTheme
import com.popcorncoders.watchly.viewmodel.MovieListViewModel

class MainActivity : ComponentActivity() {
    private val movieListViewModel: MovieListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchlyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    MovieListScreen(viewModel = movieListViewModel)
                }
            }
        }
    }
}

@Composable
fun MovieListScreen(viewModel: MovieListViewModel) {
    // Observe the movies LiveData
    val movies = viewModel.movies.observeAsState(emptyList())

    LazyColumn {
        items(movies.value) { movie ->
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


