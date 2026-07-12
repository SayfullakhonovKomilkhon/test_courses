package com.example.feature.auth.domain.usecase

import com.example.core.common.Resource
import com.example.core.network.AuthResponse
import com.example.feature.auth.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Resource<AuthResponse> {
        if (email.isBlank() || password.isBlank()) {
            return Resource.Error("Email и пароль не могут быть пустыми")
        }
        return repository.login(email, password)
    }
}
