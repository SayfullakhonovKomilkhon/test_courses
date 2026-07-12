package com.example.feature.auth.domain.repository

import com.example.core.common.Resource
import com.example.core.network.AuthResponse

interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<AuthResponse>
    suspend fun register(email: String, password: String): Resource<AuthResponse>
}
