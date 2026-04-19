package com.popcorncoders.watchly.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.popcorncoders.watchly.data.local.AppDatabase
import com.popcorncoders.watchly.data.local.entity.RatingEntity
import com.popcorncoders.watchly.data.remote.RetrofitClient
import com.popcorncoders.watchly.repository.MovieRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RatingViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)

    private val repository = MovieRepository(
        apiService = RetrofitClient.api,
        movieDao = database.movieDao(),
        favoriteDao = database.favoriteDao(),
        ratingDao = database.ratingDao()
    )

    // Observe all rated movies from DB
    val ratings: StateFlow<List<RatingEntity>> = repository.getRatings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Delete a rating
    fun removeRating(movieId: Int) {
        viewModelScope.launch {
            repository.deleteRating(movieId)
        }
    }
}