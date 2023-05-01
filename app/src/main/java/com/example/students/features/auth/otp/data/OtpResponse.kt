package com.example.students.features.auth.otp.data

data class OtpResponse(
    private val status: Int,
    private val massage: String,
    val code: String,
)