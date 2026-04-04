package com.popcorncoders.watchly.model

// Data class representing a single movie from the API
// Variable names must match the JSON response from TMDb
data class Movie (
    val id: Int, // Unique ID of the movie
    val title: String, // Movie title
    val overview: String, // Movie description or summary
    val poster_path: String // Path to the movie poster image
)
