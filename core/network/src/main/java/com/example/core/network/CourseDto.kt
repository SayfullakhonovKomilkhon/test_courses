package com.example.core.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CourseDto(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "category") val category: String,
    @Json(name = "description") val description: String
)
