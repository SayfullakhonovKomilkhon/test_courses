package com.example.data.repository

import com.example.core.common.Resource
import com.example.core.common.model.Course
import com.example.core.database.FavoriteCourseDao
import com.example.core.database.FavoriteCourseEntity
import com.example.core.network.CoursesApi
import com.example.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CourseRepositoryImpl(
    private val api: CoursesApi,
    private val dao: FavoriteCourseDao
) : CourseRepository {

    private val staticFallbackCourses = listOf(
        Course(
            id = 1,
            title = "Java-разработчик с нуля",
            category = "Программирование",
            description = "Освойте backend-разработку и программирование на Java, фреймворки...",
            price = "999 ₽",
            rating = 4.9,
            date = "22 Мая 2024",
            imageBgColor = "#FFAA00"
        ),
        Course(
            id = 2,
            title = "3D-дженералист",
            category = "Дизайн",
            description = "Освой профессию 3D-дженералиста и стань универсальным специалистом, который умеет...",
            price = "12 000 ₽",
            rating = 3.9,
            date = "10 Сентября 2024",
            imageBgColor = "#E3C6B6"
        ),
        Course(
            id = 3,
            title = "RabbitMQ для разработчиков",
            category = "Программирование",
            description = "Полный курс по обмену сообщениями, очередям и паттернам интеграции микросервисов.",
            price = "2 499 ₽",
            rating = 4.7,
            date = "15 Июня 2024",
            imageBgColor = "#2E5BFF"
        ),
        Course(
            id = 4,
            title = "Основы веб-дизайна в Figma",
            category = "Дизайн",
            description = "Научитесь проектировать современные веб-интерфейсы, создавать адаптивные сетки и интерактивные прототипы.",
            price = "1 199 ₽",
            rating = 4.5,
            date = "01 Июля 2024",
            imageBgColor = "#FF5C00"
        ),
        Course(
            id = 5,
            title = "B2B Маркетинг: Стратегия",
            category = "Маркетинг",
            description = "Пошаговое руководство по генерации лидов в сфере B2B, настройке воронки продаж и работе с крупными клиентами.",
            price = "4 500 ₽",
            rating = 4.2,
            date = "18 Августа 2024",
            imageBgColor = "#9B51E0"
        ),
        Course(
            id = 6,
            title = "Google Аналитика 4",
            category = "Аналитика",
            description = "Освойте современный инструмент веб-аналитики, отслеживание событий, создание кастомных отчетов и когортный анализ.",
            price = "3 200 ₽",
            rating = 4.6,
            date = "05 Сентября 2024",
            imageBgColor = "#F2994A"
        )
    )

    override fun getCoursesFlow(searchQuery: String, category: String): Flow<Resource<List<Course>>> {
        val remoteFlow = flow {
            emit(Resource.Loading)
            try {
                val dtos = api.getCourses()
                val courses = dtos.map { dto ->
                    val rating = when (dto.id) {
                        1 -> 4.9
                        2 -> 3.9
                        else -> 4.0 + (dto.id % 10) / 10.0
                    }
                    val date = when (dto.id) {
                        1 -> "22 Мая 2024"
                        2 -> "10 Сентября 2024"
                        else -> "12 Октября 2024"
                    }
                    val price = when (dto.id) {
                        1 -> "999 ₽"
                        2 -> "12 000 ₽"
                        else -> "5 000 ₽"
                    }
                    val bgColor = when (dto.id % 4) {
                        0 -> "#FFAA00"
                        1 -> "#E3C6B6"
                        2 -> "#2E5BFF"
                        else -> "#9B51E0"
                    }
                    Course(
                        id = dto.id,
                        title = dto.title,
                        category = dto.category,
                        description = dto.description,
                        price = price,
                        rating = rating,
                        date = date,
                        imageBgColor = bgColor
                    )
                }
                emit(Resource.Success(courses))
            } catch (e: Exception) {
                // If remote fails, return static courses as fallback
                emit(Resource.Success(staticFallbackCourses))
            }
        }

        val favoritesFlow = dao.getAllFavoritesFlow().map { list ->
            list.map { it.id }.toSet()
        }

        return combine(remoteFlow, favoritesFlow) { remoteRes, favIds ->
            if (remoteRes is Resource.Success) {
                val filtered = remoteRes.data.map { course ->
                    course.copy(isFavorite = favIds.contains(course.id))
                }.filter { course ->
                    val matchesSearch = course.title.contains(searchQuery, ignoreCase = true) ||
                            course.description.contains(searchQuery, ignoreCase = true)
                    val matchesCategory = category == "Все" || course.category.equals(category, ignoreCase = true)
                    matchesSearch && matchesCategory
                }
                Resource.Success(filtered)
            } else {
                remoteRes
            }
        }
    }

    override fun getFavoriteCoursesFlow(): Flow<List<Course>> {
        return dao.getAllFavoritesFlow().map { entities ->
            entities.map { entity ->
                val staticCourse = staticFallbackCourses.find { it.id == entity.id }
                Course(
                    id = entity.id,
                    title = entity.title,
                    category = entity.category,
                    description = staticCourse?.description ?: "",
                    isFavorite = true,
                    price = staticCourse?.price ?: "999 ₽",
                    rating = staticCourse?.rating ?: 4.9,
                    date = staticCourse?.date ?: "22 Мая 2024",
                    imageBgColor = staticCourse?.imageBgColor ?: "#FFAA00"
                )
            }
        }
    }

    override suspend fun toggleFavorite(course: Course) {
        if (course.isFavorite) {
            dao.deleteFavoriteById(course.id)
        } else {
            dao.insertFavorite(
                FavoriteCourseEntity(
                    id = course.id,
                    title = course.title,
                    category = course.category,
                    isFavorite = true
                )
            )
        }
    }
}
