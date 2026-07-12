package com.example.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.Resource
import com.example.core.common.model.Course
import com.example.domain.usecase.GetCoursesUseCase
import com.example.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModel(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Все")
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _isSortedByDate = MutableStateFlow(false)
    val isSortedByDate = _isSortedByDate.asStateFlow()

    val categories = MutableStateFlow(listOf("Все", "Программирование", "Дизайн", "Маркетинг", "Аналитика"))

    val coursesState: StateFlow<Resource<List<Course>>> = combine(
        _searchQuery,
        _selectedCategory,
        _isSortedByDate
    ) { query, category, isSorted ->
        Triple(query, category, isSorted)
    }.flatMapLatest { (query, category, isSorted) ->
        getCoursesUseCase(query, category).map { resource ->
            if (resource is Resource.Success && isSorted) {
                Resource.Success(resource.data.sortedByDescending { it.publishDate })
            } else {
                resource
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Resource.Loading
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun toggleSort() {
        _isSortedByDate.value = !_isSortedByDate.value
    }

    fun toggleFavorite(course: Course) {
        viewModelScope.launch {
            toggleFavoriteUseCase(course)
        }
    }
}
