package com.popcorncoders.watchly.repository

import com.popcorncoders.watchly.data.local.dao.MovieDao
import com.popcorncoders.watchly.data.local.entity.MovieEntity
import com.popcorncoders.watchly.data.remote.ApiService
import kotlinx.coroutines.flow.Flow

class MovieRepository (
    private val apiService: ApiService,
    private val movieDao: MovieDao,
) {
    private val apiKey = "8722193d9837f70bd611fb987c977f33"

    // Fetch movies from API and cache them in Room
    suspend fun fetchAndCacheMovies() {
        val response = apiService.getPopularMovies(apiKey)
        val entities = response.results.map { movie ->
            MovieEntity (
                id = movie.id,
                title = movie.title,
                overview = movie.overview,
                posterPath = movie.poster_path
            )
        }
        movieDao.clearAllMovies()
        movieDao.insertMovies(entities)
    }
    // Get movies from local Room database as a Flow
    fun getMoviesFromDb() : Flow<List<MovieEntity>> {
        return movieDao.getAllMovies()
    }
}