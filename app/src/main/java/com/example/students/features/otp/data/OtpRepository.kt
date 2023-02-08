package com.example.students.features.otp.data

import com.example.students.utils.Resource

interface OtpRepository {
    suspend fun sendOtp(phoneNumber: String): Resource<String>
}