package com.example.students.features.otp.data

data class OtpResponse(
    private val status: Int,
    private val massage: String,
    val code: String,
)