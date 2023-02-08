package com.example.students.features.otp.data

import com.example.students.utils.GlobalPreferences
import com.example.students.utils.Resource
import com.example.students.utils.exceptions.NetworkExceptions
import java.lang.Exception

class OtpRepositoryImpl(
    private val otpApi: OtpApi,
    private val preferences: GlobalPreferences,
) : OtpRepository {

    override suspend fun sendOtp(phoneNumber: String): Resource<String> {
        return try {
            val result = otpApi.sendOtp(
                phoneNumber = phoneNumber
            )
            val responseBody = result.body()
            if (result.isSuccessful && responseBody != null && responseBody.code.isNotBlank()) {
                Resource.success(responseBody.code)
            } else {
                Resource.error(NetworkExceptions.BadRequest(result.message()))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during registration"))
        }
    }
}