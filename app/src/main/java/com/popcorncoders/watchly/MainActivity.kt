package com.popcorncoders.watchly

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.popcorncoders.watchly.model.Movie
import com.popcorncoders.watchly.notification.NotificationHelper
import com.popcorncoders.watchly.ui.FavoritesScreen
import com.popcorncoders.watchly.ui.MovieDetailScreen
import com.popcorncoders.watchly.ui.MovieListScreen
import com.popcorncoders.watchly.ui.RatedMoviesScreen
import com.popcorncoders.watchly.ui.theme.WatchlyTheme
import com.popcorncoders.watchly.ui.theme.Screen
import com.popcorncoders.watchly.viewmodel.FavoriteViewModel
import com.popcorncoders.watchly.viewmodel.MovieDetailViewModel
import com.popcorncoders.watchly.viewmodel.MovieListViewModel
import com.popcorncoders.watchly.viewmodel.RatingViewModel

class MainActivity : ComponentActivity() {

    private val movieListViewModel: MovieListViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val movieDetailViewModel: MovieDetailViewModel by viewModels()
    private val ratingViewModel: RatingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createNotificationChannel(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        // Notify user when they leave the app
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onStart(owner: LifecycleOwner) {
                    // App came to foreground - reset flag
                }

                override fun onStop(owner: LifecycleOwner) {
                    // Add a small delay to confirm app went to background
                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                            if (!ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(
                                androidx.lifecycle.Lifecycle.State.STARTED
                            )) {
                                val favoriteCount = favoriteViewModel.favorites.value.size
                                if (favoriteCount > 0) {
                                    NotificationHelper.showFavoritesReminderNotification(
                                        context = applicationContext,
                                        favoriteCount = favoriteCount
                                    )
                                }
                            }
                        },
                        500L
                    )
                }
            }
        )

        setContent {
            var isDarkMode by rememberSaveable { mutableStateOf(false) }

            WatchlyTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()

                val movies by movieListViewModel.movies.collectAsStateWithLifecycle(emptyList())
                val favoriteMovieIds by movieListViewModel.favoriteMovieIds.collectAsStateWithLifecycle()
                val error by movieListViewModel.error.collectAsStateWithLifecycle()
                val favorites by favoriteViewModel.favorites.collectAsStateWithLifecycle(emptyList())
                val ratings by ratingViewModel.ratings.collectAsStateWithLifecycle(emptyList())

                val mappedMovies = movies.map { entity ->
                    Movie(
                        id = entity.id,
                        title = entity.title,
                        overview = entity.overview,
                        poster_path = entity.posterPath,
                        backdrop_path = entity.backdropPath,
                        release_date = entity.releaseDate,
                        vote_average = entity.voteAverage,
                        vote_count = 0,
                        popularity = entity.popularity,
                        genre_ids = emptyList()
                    )
                }

                fun goHome() {
                    navController.navigate("movie_list") {
                        popUpTo("movie_list") {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = "movie_list"
                ) {
                    composable("movie_list") {
                        MovieListScreen(
                            movies = mappedMovies,
                            favoriteMovieIds = favoriteMovieIds,
                            currentScreen = Screen.HOME,
                            errorMessage = error,
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { isDarkMode = !isDarkMode },

                            onHomeClick = {
                                navController.popBackStack("movie_list", false)
                            },

                            onMovieClick = { movieId ->
                                navController.navigate("movie_detail/$movieId")
                            },

                            onFavoriteClick = { movie ->
                                movieListViewModel.toggleFavorite(movie)
                            },

                            onFavoritesPageClick = {
                                navController.navigate("favorites")
                            },

                            onRatedMoviesPageClick = {
                                navController.navigate("rated_movies")
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

                        MovieDetailScreen(
                            movie = selectedMovie,
                            viewModel = movieDetailViewModel,
                            currentScreen = Screen.DETAIL,
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { isDarkMode = !isDarkMode },
                            onHomeClick = {
                                goHome()
                            },
                            onFavoritesPageClick = {
                                navController.navigate("favorites")
                            },
                            onRatedMoviesPageClick = {
                                navController.navigate("rated_movies")
                            },
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("favorites") {
                        FavoritesScreen(
                            favorites = favorites,
                            isLoading = false,
                            errorMessage = null,
                            currentScreen = Screen.FAVORITES,
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { isDarkMode = !isDarkMode },
                            onHomeClick = {
                                goHome()
                            },
                            onRatedMoviesPageClick = {
                                navController.navigate("rated_movies")
                            },
                            onRemoveClick = { movieId ->
                                favoriteViewModel.removeFavorite(movieId)
                            },
                            onMovieClick = { movieId ->
                                navController.navigate("movie_detail/$movieId")
                            },
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("rated_movies") {
                        RatedMoviesScreen(
                            ratedMovies = ratings,
                            onRemoveRating = { movieId -> ratingViewModel.removeRating(movieId) },
                            currentScreen = Screen.RATED,
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { isDarkMode = !isDarkMode },
                            onHomeClick = {
                                goHome()
                            },
                            onRatedMoviesPageClick = {
                                navController.navigate("rated_movies")
                            },
                            onFavoritesPageClick = {
                                navController.navigate("favorites")
                            },
                            onMovieClick = { movieId ->
                                navController.navigate("movie_detail/$movieId")
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