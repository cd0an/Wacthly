package com.popcorncoders.watchly.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.popcorncoders.watchly.data.remote.RetrofitClient
import com.popcorncoders.watchly.model.Movie
import kotlinx.coroutines.launch


class MovieListViewModel : ViewModel() {
    // LiveData for the list of movies
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    // LiveData for errors
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // TMDb API Key
    private val apiKey = "8722193d9837f70bd611fb987c977f33"

    init {
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                // Make the API call using Retrofit
                val response = RetrofitClient.api.getPopularMovies(apiKey)
                _movies.value = response.results

                // Log for testing
                Log.d("MoviesListViewModel", "Number of movies received: ${response.results.size}")
                response.results.forEach { movie ->
                    Log.d("MovieListViewModel", "Movie: ${movie.title}")
                }
            }
            catch (e: Exception) {
                _error.value = e.message
                Log.e("MovieListViewModel", "Error fetching movies", e)
            }
        }
    }
}