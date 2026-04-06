package com.popcorncoders.watchly.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.popcorncoders.watchly.data.local.AppDatabase
import com.popcorncoders.watchly.data.local.entity.FavoriteEntity
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

    // Check if a movie is in the favorites list by ID
    fun checkIfFavorite(movieId: Int) {
        viewModelScope.launch {
            _isFavorite.value = dao.getFavoriteByMovieId(movieId) != null
        }
    }

    // Add or remove a movie from favorites
    fun toggleFavorite(movie: FavoriteEntity) {
        viewModelScope.launch {         // Launch a coroutine to to database work
            val existing = dao.getFavoriteByMovieId(movie.movieId)
            if (existing == null) {
                dao.addFavorite(movie)  // Add movie to database
                _isFavorite.value = true         // Update state to show it is now a favorite
            } else {
                dao.removeFavorite(movie.movieId)   // Remove movie from database
                _isFavorite.value = false           // Update state to show it's not a favorite
            }
        }
    }
}