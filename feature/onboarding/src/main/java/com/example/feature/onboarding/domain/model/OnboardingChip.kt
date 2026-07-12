package com.example.feature.onboarding.domain.model

data class OnboardingChip(
    val id: String,
    val text: String,
    val isHighlighted: Boolean,
    val rotation: Float = 0f
)
