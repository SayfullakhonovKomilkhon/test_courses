package com.example.domain.usecase

import com.example.core.common.model.Course
import com.example.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(private val repository: CourseRepository) {
    operator fun invoke(): Flow<List<Course>> {
        return repository.getFavoriteCoursesFlow()
    }
}
