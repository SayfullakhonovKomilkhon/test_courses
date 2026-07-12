package com.example.core.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CoursesApi {
    @GET("https://drive.google.com/uc?id=15arTK7XT2b7Yv4BJsmDctA4Hg-BbS8-q&export=download")
    suspend fun getCourses(): CoursesResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}
