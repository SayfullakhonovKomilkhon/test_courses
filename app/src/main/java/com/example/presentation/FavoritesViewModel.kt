package com.example.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.model.Course
import com.example.domain.usecase.GetFavoritesUseCase
import com.example.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    val favoritesState: StateFlow<List<Course>> = getFavoritesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavorite(course: Course) {
        viewModelScope.launch {
            toggleFavoriteUseCase(course)
        }
    }
}
