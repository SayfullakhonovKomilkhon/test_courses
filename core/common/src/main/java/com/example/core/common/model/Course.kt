package com.example.core.common.model

data class Course(
    val id: Int,
    val title: String,
    val category: String,
    val description: String,
    val isFavorite: Boolean = false,
    val price: String = "999 ₽",
    val rating: Double = 4.9,
    val date: String = "22 Мая 2024",
    val imageBgColor: String = "#FFAA00",
    val publishDate: String = "2024-01-01"
)
