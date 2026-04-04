package com.popcorncoders.watchly.data.remote

//
interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies (
        @Query("api_key") apiKey: String
    ): MovieResponse
}