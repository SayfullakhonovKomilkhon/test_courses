package com.example.feature.auth.di

import com.example.feature.auth.data.repository.AuthRepositoryImpl
import com.example.feature.auth.domain.repository.AuthRepository
import com.example.feature.auth.domain.usecase.LoginUseCase
import com.example.feature.auth.domain.usecase.RegisterUseCase
import com.example.feature.auth.presentation.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(api = get()) }
    single { LoginUseCase(repository = get()) }
    single { RegisterUseCase(repository = get()) }
    viewModel { AuthViewModel(loginUseCase = get(), registerUseCase = get()) }
}
