package com.example.feature.auth.domain.usecase

import com.example.core.common.Resource
import com.example.core.network.AuthResponse
import com.example.feature.auth.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, repeatPassword: String): Resource<AuthResponse> {
        if (email.isBlank() || password.isBlank() || repeatPassword.isBlank()) {
            return Resource.Error("Заполните все поля")
        }
        if (password != repeatPassword) {
            return Resource.Error("Пароли не совпадают")
        }
        return repository.register(email, password)
    }
}
