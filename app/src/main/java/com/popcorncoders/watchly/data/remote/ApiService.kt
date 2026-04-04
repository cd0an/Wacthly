package com.popcorncoders.watchly.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import com.popcorncoders.watchly.model.MovieResponse

// Defines all API endpoints
// Retrofit will automatically generate the implementation
interface ApiService {
    // Makes a GET request to: https://api.themoviedb.org/3/movie/popular
    // api_key passed as a query parameter in the URL
    // suspend allows this to run asynchronously using Kotlin Coroutines
    @GET("movie/popular")
    suspend fun getPopularMovies (
        @Query("api_key") apiKey: String
    ): MovieResponse
}

