package com.popcorncoders.watchly.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton object to provide a single instance of Retrofit across the app
object RetrofitClient {
    // Base URL for all TMDb API requests
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    // Lazy initialization ensures Retrofit is only created when needed
    val api: ApiService by lazy {
        Retrofit.Builder() // Build the Retrofit instance
            .baseUrl(BASE_URL) // Set the base URL
            .addConverterFactory(GsonConverterFactory.create()) // Converts JSON responses into Kotlin data classes
            .build()
            .create(ApiService::class.java) // Creates an implementation of ApiService interface
    }
}

