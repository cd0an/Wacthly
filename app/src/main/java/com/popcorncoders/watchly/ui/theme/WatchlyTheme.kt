package com.popcorncoders.watchly.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Define the color schemes (can customize)
private val LightColors = lightColorScheme()
private val DarkColors = darkColorScheme()

@Composable
fun WatchlyTheme (
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme (
        colorScheme = colors,
        typography = MaterialTheme.typography,
        content = content
    )
}