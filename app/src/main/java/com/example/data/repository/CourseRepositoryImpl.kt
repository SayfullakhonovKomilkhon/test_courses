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
            id = 100,
            title = "Java-разработчик с нуля",
            category = "Программирование",
            description = "Освойте backend-разработку и программирование на Java, фреймворки Spring и Maven, работу с базами данных и API. Создайте свой собственный проект, собрав портфолио и став востребованным специалистом для любой IT компании.",
            price = "999 ₽",
            rating = 4.9,
            date = "22 Мая 2024",
            imageBgColor = "#FFAA00",
            publishDate = "2024-02-02"
        ),
        Course(
            id = 101,
            title = "3D-дженералист",
            category = "Дизайн",
            description = "Освой профессию 3D-дженералиста и стань универсальным специалистом, который умеет создавать 3D-модели, текстуры и анимации, а также может строить карьеру в геймдеве, кино, рекламе или дизайне.",
            price = "12 000 ₽",
            rating = 3.9,
            date = "10 Сентября 2024",
            imageBgColor = "#E3C6B6",
            publishDate = "2024-01-20"
        ),
        Course(
            id = 102,
            title = "Python Advanced. Для продвинутых",
            category = "Программирование",
            description = "Вы узнаете, как разрабатывать гибкие и высокопроизводительные серверные приложения на языке Kotlin. Преподаватели на вебинарах покажут пример того, как разрабатывается проект маркетплейса: от идеи и постановки задачи – до конечного решения",
            price = "1 299 ₽",
            rating = 4.3,
            date = "12 Октября 2024",
            imageBgColor = "#2E5BFF",
            publishDate = "2024-08-10"
        ),
        Course(
            id = 103,
            title = "Системный аналитик",
            category = "Аналитика",
            description = "Освоите навыки системной аналитики с нуля за 9 месяцев. Будет очень много практики на реальных проектах, чтобы вы могли сразу стартовать в IT.",
            price = "1 199 ₽",
            rating = 4.5,
            date = "15 Апреля 2024",
            imageBgColor = "#9B51E0",
            publishDate = "2024-01-13"
        ),
        Course(
            id = 104,
            title = "Аналитик данных",
            category = "Аналитика",
            description = "В этом уроке вы узнаете, кто такой аналитик данных и какие задачи он решает. А главное — мы расскажем, чему вы научитесь по завершении программы обучения профессии «Аналитик данных».",
            price = "899 ₽",
            rating = 4.7,
            date = "20 Июня 2024",
            imageBgColor = "#FFAA00",
            publishDate = "2024-03-12"
        )
    )

    private fun formatJsonDate(dateStr: String): String {
        val parts = dateStr.split("-")
        if (parts.size != 3) return dateStr
        val month = when (parts[1]) {
            "01" -> "Января"
            "02" -> "Февраля"
            "03" -> "Марта"
            "04" -> "Апреля"
            "05" -> "Мая"
            "06" -> "Июня"
            "07" -> "Июля"
            "08" -> "Августа"
            "09" -> "Сентября"
            "10" -> "Октября"
            "11" -> "Ноября"
            "12" -> "Декабря"
            else -> parts[1]
        }
        val day = parts[2].toIntOrNull()?.toString() ?: parts[2]
        return "$day $month ${parts[0]}"
    }

    private fun getCategoryForTitle(title: String): String {
        return when {
            title.contains("Java", ignoreCase = true) || title.contains("Python", ignoreCase = true) -> "Программирование"
            title.contains("3D", ignoreCase = true) -> "Дизайн"
            title.contains("Аналитик", ignoreCase = true) -> "Аналитика"
            else -> "Программирование"
        }
    }

    override fun getCoursesFlow(searchQuery: String, category: String): Flow<Resource<List<Course>>> {
        val remoteFlow = flow {
            emit(Resource.Loading)
            try {
                val response = api.getCourses()

                // Pre-populate database with initial likes if DB is empty
                try {
                    val currentFavs = dao.getAllFavorites()
                    if (currentFavs.isEmpty()) {
                        response.courses.filter { it.hasLike }.forEach { dto ->
                            dao.insertFavorite(
                                FavoriteCourseEntity(
                                    id = dto.id,
                                    title = dto.title,
                                    category = getCategoryForTitle(dto.title),
                                    isFavorite = true
                                )
                            )
                        }
                    }
                } catch (dbEx: Exception) {
                    dbEx.printStackTrace()
                }

                val courses = response.courses.map { dto ->
                    val rating = dto.rate.toDoubleOrNull() ?: 4.0
                    val date = formatJsonDate(dto.startDate)
                    val price = "${dto.price} ₽"
                    val courseCategory = getCategoryForTitle(dto.title)
                    val bgColor = when (dto.id % 4) {
                        0 -> "#FFAA00"
                        1 -> "#E3C6B6"
                        2 -> "#2E5BFF"
                        else -> "#9B51E0"
                    }
                    Course(
                        id = dto.id,
                        title = dto.title,
                        category = courseCategory,
                        description = dto.text,
                        price = price,
                        rating = rating,
                        date = date,
                        imageBgColor = bgColor,
                        publishDate = dto.publishDate
                    )
                }
                emit(Resource.Success(courses))
            } catch (e: Exception) {
                // If remote fails, return static courses as fallback and pre-populate DB if empty
                try {
                    val currentFavs = dao.getAllFavorites()
                    if (currentFavs.isEmpty()) {
                        staticFallbackCourses.filter { it.id == 100 || it.id == 104 }.forEach { course ->
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
                } catch (dbEx: Exception) {
                    dbEx.printStackTrace()
                }
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
                    imageBgColor = staticCourse?.imageBgColor ?: "#FFAA00",
                    publishDate = staticCourse?.publishDate ?: "2024-01-01"
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
