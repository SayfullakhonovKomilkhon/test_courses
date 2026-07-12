package com.example.core.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCourseDao {
    @Query("SELECT * FROM favorite_courses")
    fun getAllFavoritesFlow(): Flow<List<FavoriteCourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(course: FavoriteCourseEntity)

    @Delete
    suspend fun deleteFavorite(course: FavoriteCourseEntity)

    @Query("DELETE FROM favorite_courses WHERE id = :id")
    suspend fun deleteFavoriteById(id: Int)
}
