package com.example.domain.repository

import com.example.core.common.Resource
import com.example.core.common.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun getCoursesFlow(searchQuery: String, category: String): Flow<Resource<List<Course>>>
    fun getFavoriteCoursesFlow(): Flow<List<Course>>
    suspend fun toggleFavorite(course: Course)
}
