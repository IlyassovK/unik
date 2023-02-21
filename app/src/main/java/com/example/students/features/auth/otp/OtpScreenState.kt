package com.example.students.features.auth.otp

sealed class OtpScreenState {
    object Loading : OtpScreenState()
    class Exception(val message: String) : OtpScreenState()
    object OtpResultSuccess : OtpScreenState()
    object OtpResultFailed : OtpScreenState()
    object RegistrationSuccess: OtpScreenState()
    object Success : OtpScreenState()
    object Failed : OtpScreenState()
}
