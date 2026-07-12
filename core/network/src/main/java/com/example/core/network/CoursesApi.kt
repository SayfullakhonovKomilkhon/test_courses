package com.example.core.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CoursesApi {
    @GET("courses")
    suspend fun getCourses(): List<CourseDto>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}
