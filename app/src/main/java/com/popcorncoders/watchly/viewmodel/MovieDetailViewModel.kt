package com.popcorncoders.watchly.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.popcorncoders.watchly.data.local.AppDatabase
import com.popcorncoders.watchly.data.local.entity.FavoriteEntity
import com.popcorncoders.watchly.data.remote.RetrofitClient
import com.popcorncoders.watchly.model.Movie
import com.popcorncoders.watchly.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)

    private val repository = MovieRepository(
        apiService = RetrofitClient.api,
        movieDao = database.movieDao(),
        favoriteDao = database.favoriteDao()
    )

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _rating = MutableStateFlow(0)
    val rating: StateFlow<Int> = _rating

    fun loadFavorite(movieId: Int) {
        viewModelScope.launch {
            val favorite = repository.getFavoriteByMovieId(movieId)
            _isFavorite.value = favorite != null
            _rating.value = favorite?.rating ?: 0
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            val existing = repository.getFavoriteByMovieId(movie.id)

            if (existing == null) {
                repository.upsertFavorite(
                    FavoriteEntity(
                        movieId = movie.id,
                        title = movie.title,
                        overview = movie.overview,
                        posterPath = movie.poster_path,
                        rating = _rating.value
                    )
                )
                _isFavorite.value = true
            } else {
                repository.deleteFavorite(movie.id)
                _isFavorite.value = false
            }
        }
    }

    fun updateRating(value: Int, movie: Movie) {
        viewModelScope.launch {
            repository.upsertFavorite(
                FavoriteEntity(
                    movieId = movie.id,
                    title = movie.title,
                    overview = movie.overview,
                    posterPath = movie.poster_path,
                    rating = value
                )
            )
            _rating.value = value
            _isFavorite.value = true
        }
    }
}