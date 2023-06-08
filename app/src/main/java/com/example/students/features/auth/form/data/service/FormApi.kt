package com.example.students.features.auth.form.data.service

import com.example.students.features.auth.form.data.model.FormDataResponse
import com.example.students.features.auth.form.data.model.ProfileRequest
import com.example.students.features.auth.form.data.model.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FormApi {

    @POST("/api/user/profile")
    suspend fun updateProfile(
        @Body profileRequest: ProfileRequest,
    ): Response<String>

    @GET("/api/university/index")
    suspend fun getUniversities(): FormDataResponse

    @GET("/api/hobby/index")
    suspend fun getHobbies(): FormDataResponse

    @GET("/api/speciality/index")
    suspend fun getSpecialities(): FormDataResponse

    @GET("/api/city/index")
    suspend fun getCities(): FormDataResponse
}