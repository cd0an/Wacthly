package com.popcorncoders.watchly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.popcorncoders.watchly.model.Movie
import com.popcorncoders.watchly.ui.FavoritesScreen
import com.popcorncoders.watchly.ui.MovieDetailScreen
import com.popcorncoders.watchly.ui.MovieListScreen
import com.popcorncoders.watchly.ui.theme.WatchlyTheme
import com.popcorncoders.watchly.viewmodel.MovieListViewModel
import com.popcorncoders.watchly.viewmodel.FavoriteViewModel
import com.popcorncoders.watchly.viewmodel.MovieDetailViewModel

class MainActivity : ComponentActivity() {

    private val movieListViewModel: MovieListViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WatchlyTheme {
                val navController = rememberNavController()

                val movies by movieListViewModel.movies.collectAsStateWithLifecycle(emptyList())
                val error by movieListViewModel.error.collectAsStateWithLifecycle()
                val favorites by favoriteViewModel.favorites.collectAsState()

                // Map MovieEntity -> Movie before passing to UI
                val mappedMovies = movies.map { entity ->
                    Movie(
                        id = entity.id,
                        title = entity.title,
                        overview = entity.overview,
                        poster_path = entity.posterPath
                    )
                }

                NavHost(
                    navController = navController,
                    startDestination = "movie_list"
                ) {
                    composable("movie_list") {
                        MovieListScreen(
                            movies = mappedMovies,
                            errorMessage = error,
                            onMovieClick = { movie ->
                                navController.navigate("movie_detail/${movie.id}")
                            },
                            onFavoritesClick = {
                                navController.navigate("favorites")
                            }
                        )
                    }

                    composable(
                        route = "movie_detail/{movieId}",
                        arguments = listOf(
                            navArgument("movieId") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val movieId = backStackEntry.arguments?.getInt("movieId") ?: -1
                        val selectedMovie: Movie? = mappedMovies.find { it.id == movieId }
                        val movieDetailViewModel: MovieDetailViewModel = viewModel(backStackEntry)

                        MovieDetailScreen(
                            movie = selectedMovie,
                            viewModel = movieDetailViewModel,
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("favorites") {
                        FavoritesScreen(
                            favorites = favorites,
                            onRemoveClick = { movieId ->
                                favoriteViewModel.removeFavorite(movieId)

                            },
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}