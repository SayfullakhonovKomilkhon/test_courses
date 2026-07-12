package com.example.feature.onboarding.di

import com.example.feature.onboarding.presentation.OnboardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val onboardingModule = module {
    viewModel { OnboardingViewModel() }
}
