package com.popcorncoders.watchly.model

// Represents the full response from the TMDb API
// API returns a list of movies inside a "results" field
data class MovieResponse (
    val results: List<Movie> // List of Movie objects
)