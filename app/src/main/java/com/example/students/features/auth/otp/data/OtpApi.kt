package com.example.students.features.auth.otp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OtpApi {
    @GET("/api/send/otp/{phone}")
    suspend fun sendOtp(@Path("phone") phoneNumber: String): Response<OtpResponse>
}