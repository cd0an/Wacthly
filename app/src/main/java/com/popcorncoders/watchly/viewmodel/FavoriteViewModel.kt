package com.popcorncoders.watchly.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel   // ViewModel that has access to the Application context
import androidx.lifecycle.viewModelScope     // Scope for running coroutines tied to ViewModel
import com.popcorncoders.watchly.data.local.AppDatabase
import com.popcorncoders.watchly.data.local.entity.FavoriteEntity
import com.popcorncoders.watchly.notification.NotificationHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


// ViewModel handles the favorite movies for the app.
// AndroidViewModel is like a regular ViewModel but also gives access to the app's context,
// which need to get the database instance.
class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    // Get the DAO  to interact with the favorites table in the database
    private val dao = AppDatabase.getDatabase(application).favoriteDao()

    // Observe favorites list stored in the database.
    val favorites = dao.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addFavorite(movie: FavoriteEntity) {
        viewModelScope.launch {
            dao.insertFavorite(movie)

            // Get updated count and show notification
            val count = dao.getAllFavorites().first().size
            NotificationHelper.showFavoritesReminderNotification(
                context = getApplication(),
                favoriteCount = count
            )
        }
    }

    fun removeFavorite(movieId: Int) {
        viewModelScope.launch {
            dao.deleteFavorite(movieId)
        }
    }
}