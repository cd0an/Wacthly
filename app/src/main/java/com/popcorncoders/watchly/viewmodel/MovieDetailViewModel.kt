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

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {

    // Favorites database
    private val dao = AppDatabase.getDatabase(application).favoriteDao()

    // True only if movie is in favorites table
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    // Current rating for the movie being viewed
    private val _rating = MutableStateFlow(0)
    val rating: StateFlow<Int> = _rating

    // Separate list for rated movies
    private val _ratedMovies = MutableStateFlow<List<FavoriteEntity>>(emptyList())
    val ratedMovies: StateFlow<List<FavoriteEntity>> = _ratedMovies

    fun loadFavorite(movieId: Int) {
        viewModelScope.launch {
            val favorite = dao.getFavoriteByMovieId(movieId)
            _isFavorite.value = favorite != null

            val ratedMovie = _ratedMovies.value.find { it.movieId == movieId }
            _rating.value = ratedMovie?.rating ?: 0
        }
    }

    fun updateRating(value: Int, movie: Movie) {
        viewModelScope.launch {
            val existing = _ratedMovies.value.find { it.movieId == movie.id }

            val ratedMovie = FavoriteEntity(
                movieId = movie.id,
                title = movie.title,
                overview = movie.overview,
                posterPath = movie.poster_path,
                backdropPath = movie.backdrop_path,
                releaseDate = movie.release_date,
                voteAverage = movie.vote_average,
                rating = value
            )

            _ratedMovies.value =
                if (existing == null) {
                    _ratedMovies.value + ratedMovie
                } else {
                    _ratedMovies.value.map {
                        if (it.movieId == movie.id) ratedMovie else it
                    }
                }

            _rating.value = value
        }
    }
}