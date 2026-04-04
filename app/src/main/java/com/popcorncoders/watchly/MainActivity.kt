package com.popcorncoders.watchly

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.popcorncoders.watchly.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val tag = "APITest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiKey = "8722193d9837f70bd611fb987c977f33"

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getPopularMovies(apiKey)
                Log.d(tag, "Number of movies received: ${response.results.size}")
                response.results.forEach { movie ->
                    Log.d(tag, "Movie: ${movie.title}")
                }
            }
            catch (e: Exception) {
                Log.e(tag, "API call failed", e)
            }
        }
    }
}


