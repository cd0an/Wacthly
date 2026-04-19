package com.popcorncoders.watchly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.popcorncoders.watchly.data.local.entity.RatingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RatingDao {
    // Insert or update a rating
    @Insert(onConflict = OnConflictStrategy .REPLACE)
    suspend fun insertRating(rating: RatingEntity)

    // Delete a rating by movie ID
    @Query("DELETE FROM ratings WHERE movieID = :movieId")
    suspend fun deleteRating(movieId: Int)

    // Get all rated movies ordered by most recently rated
    @Query("SELECT * FROM ratings ORDER BY ratedAt DESC")
    fun getAllRatings(): Flow<List<RatingEntity>>

    // Get a specific rating by movie ID
    @Query("SELECT * FROM ratings WHERE movieId = :movieId LIMIT 1")
    suspend fun getRatingByMovieId(movieId: Int): RatingEntity?
}