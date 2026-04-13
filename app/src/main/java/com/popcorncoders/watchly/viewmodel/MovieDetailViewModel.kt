package com.popcorncoders.watchly.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.popcorncoders.watchly.data.local.AppDatabase
import com.popcorncoders.watchly.data.local.entity.FavoriteEntity
import com.popcorncoders.watchly.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// It keeps track of data for the Movie Detail screen
// It remembers the data even if the screen is rotated or recreated
class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {

    // Get the Favorite DAO to access the database
    private val dao = AppDatabase.getDatabase(application).favoriteDao()

    // To hold whether the movie is a favorite (true/false)
    private val _isFavorite = MutableStateFlow(false)

    // Public read-only version of _isFavorite
    val isFavorite: StateFlow<Boolean> = _isFavorite

    // private rating state
    private val _rating = MutableStateFlow(0)

    // Public read-only version of rating
    val rating: StateFlow<Int> = _rating


    fun loadFavorite(movieId: Int) {
        // Start a coroutine tied to the ViewModel lifecycle
        viewModelScope.launch {

            // Query the database to find if this movie is already saved in favorites
            val favorite = dao.getFavoriteByMovieId(movieId)

            // If the movie exists in favorites, set isFavorite = true
            _isFavorite.value = favorite !=null

            // If the movie is found in the database
            if (favorite !=null) {
                _rating.value = favorite.rating
            } else {
                _rating.value = 0
            }
        }
    }

    fun updateRating(value: Int, movie: Movie) {
        viewModelScope.launch {

            val newFavorite = FavoriteEntity(
                movieId = movie.id,
                title = movie.title,
                overview = movie.overview,
                posterPath = movie.poster_path,
                rating = value
            )

            dao.addFavorite(newFavorite)

            _rating.value = value
            _isFavorite.value = true
        }

    }
}