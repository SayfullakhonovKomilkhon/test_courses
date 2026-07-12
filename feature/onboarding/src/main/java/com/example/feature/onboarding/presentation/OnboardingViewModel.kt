package com.example.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import com.example.feature.onboarding.domain.model.OnboardingChip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingViewModel : ViewModel() {

    private val _chips = MutableStateFlow<List<OnboardingChip>>(emptyList())
    val chips: StateFlow<List<OnboardingChip>> = _chips.asStateFlow()

    init {
        loadChips()
    }

    private fun loadChips() {
        val list = listOf(
            OnboardingChip("1", "С Администрирование", isHighlighted = false),
            OnboardingChip("2", "RabbitMQ", isHighlighted = true, rotation = -10f),
            OnboardingChip("3", "Трафик", isHighlighted = false),
            OnboardingChip("4", "Контент маркетинг", isHighlighted = false),
            OnboardingChip("5", "B2B маркетинг", isHighlighted = false),
            OnboardingChip("6", "Google Аналитика", isHighlighted = false),
            OnboardingChip("7", "UX исследователь", isHighlighted = false),
            OnboardingChip("8", "Веб-аналитика", isHighlighted = false),
            OnboardingChip("9", "Big Data", isHighlighted = true, rotation = 8f),
            OnboardingChip("10", "Дизайн", isHighlighted = false),
            OnboardingChip("11", "Веб-дизайн", isHighlighted = false),
            OnboardingChip("12", "Cinema 4D", isHighlighted = false),
            OnboardingChip("13", "Промпт инжиниринг", isHighlighted = false),
            OnboardingChip("14", "Three.js", isHighlighted = true, rotation = -12f),
            OnboardingChip("15", "Парсинг", isHighlighted = false),
            OnboardingChip("16", "Python-разработчик", isHighlighted = false)
        )
        _chips.value = list
    }
}
