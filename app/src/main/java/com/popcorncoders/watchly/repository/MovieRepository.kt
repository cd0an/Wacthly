package com.popcorncoders.watchly.repository

import com.popcorncoders.watchly.data.local.dao.MovieDao
import com.popcorncoders.watchly.data.local.entity.MovieEntity
import com.popcorncoders.watchly.data.remote.ApiService
import kotlinx.coroutines.flow.Flow

class MovieRepository (
    private val apiService: ApiService,
    private val movieDao: MovieDao,
) {
    // Fetch movies from API and cache them in Room
    suspend fun fetchAndCacheMovies() {
        val response = apiService.getPopularMovies()
        val entities = response.results.map { movie ->
            MovieEntity (
                id = movie.id,
                title = movie.title,
                overview = movie.overview,
                posterPath = movie.poster_path,
                backdropPath = movie.backdrop_path,
                releaseDate = movie.release_date,
                voteAverage = movie.vote_average,
                popularity = movie.popularity
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