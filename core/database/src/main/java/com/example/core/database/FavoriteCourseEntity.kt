package com.example.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_courses")
data class FavoriteCourseEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val category: String,
    val isFavorite: Boolean
)
