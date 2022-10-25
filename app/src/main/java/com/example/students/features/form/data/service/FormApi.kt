package com.example.students.features.form.data.service

import com.example.students.features.form.data.model.ProfileRequest
import com.example.students.features.form.data.model.ProfileResponse
import com.example.students.features.login.data.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FormApi {

    @POST("/api/user/profile/{id}")
    suspend fun updateProfile(@Path("id") id: Long, @Body profileRequest: ProfileRequest): ProfileResponse
}