package com.popcorncoders.watchly.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.popcorncoders.watchly.data.local.AppDatabase
import com.popcorncoders.watchly.data.local.entity.MovieEntity
import com.popcorncoders.watchly.repository.MovieRepository
import com.popcorncoders.watchly.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MovieListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MovieRepository(
        apiService = RetrofitClient.api,
        movieDao = AppDatabase.getDatabase(application).movieDao()
    )

    // StateFlow for the list of movies
    private val _movies = MutableStateFlow<List<MovieEntity>>(emptyList())
    val movies: StateFlow<List<MovieEntity>> = _movies

    // StateFlow for errors
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                // Fetch from API and save to Room
                repository.fetchAndCacheMovies()
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("MovieListViewModel", "Error fetching movies", e)
            }
            // Always observe from local DB
            repository.getMoviesFromDb().collect { cachedMovies ->
                _movies.value = cachedMovies
                Log.d("MovieListViewModel", "Movies from DB: ${cachedMovies.size}")
            }
        }
    }
}