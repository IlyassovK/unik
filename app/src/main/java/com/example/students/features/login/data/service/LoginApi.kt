package com.example.students.features.login.data.service

import com.example.students.features.login.data.model.LoginRequest
import com.example.students.features.login.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginApi {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET
    suspend fun logout()
}