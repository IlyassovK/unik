package com.example.students.features.auth.otp.data

import com.example.students.features.auth.otp.data.OtpResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OtpApi {
    @GET("/api/send/otp/{phone}")
    suspend fun sendOtp(@Path("phone") phoneNumber: String): Response<OtpResponse>
}