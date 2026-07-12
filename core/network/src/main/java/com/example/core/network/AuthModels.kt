package com.example.core.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class AuthResponse(
    val token: String,
    val email: String
)
