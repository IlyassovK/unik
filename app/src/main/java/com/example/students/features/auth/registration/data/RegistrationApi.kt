package com.example.students.features.auth.registration.data

import com.example.students.features.auth.registration.data.model.RegistrationRequest
import com.example.students.features.auth.registration.data.model.RegistrationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistrationApi {
    @POST("/api/user")
    suspend fun registration(@Body request: RegistrationRequest): Response<RegistrationResponse>
}