package com.popcorncoders.watchly.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.popcorncoders.watchly.data.local.dao.FavoriteDao
import com.popcorncoders.watchly.data.local.dao.MovieDao
import com.popcorncoders.watchly.data.local.dao.RatingDao
import com.popcorncoders.watchly.data.local.entity.FavoriteEntity
import com.popcorncoders.watchly.data.local.entity.MovieEntity
import com.popcorncoders.watchly.data.local.entity.RatingEntity

@Database(
    entities = [MovieEntity::class, FavoriteEntity::class, RatingEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun ratingDao(): RatingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "watchly_database"
                )
                    //Deletes old database and recreates it when schema changes
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}