package com.example.students.features.auth.otp.domain

import com.example.students.features.auth.otp.data.OtpRepository
import com.example.students.utils.Resource

class OtpUseCase(
    private val otpRepository: OtpRepository
) {

    suspend fun sendOtp(phoneNumber: String): Resource<String>{
        return otpRepository.sendOtp(phoneNumber)
    }
}