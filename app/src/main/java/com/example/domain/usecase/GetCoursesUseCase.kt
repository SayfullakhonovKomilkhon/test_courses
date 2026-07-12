package com.example.domain.usecase

import com.example.core.common.Resource
import com.example.core.common.model.Course
import com.example.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow

class GetCoursesUseCase(private val repository: CourseRepository) {
    operator fun invoke(searchQuery: String, category: String): Flow<Resource<List<Course>>> {
        return repository.getCoursesFlow(searchQuery, category)
    }
}
