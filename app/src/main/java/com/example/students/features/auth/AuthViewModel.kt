package com.example.students.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.auth.login.data.model.LoginRequest
import com.example.students.features.auth.login.domain.usecase.LoginUseCase
import com.example.students.features.auth.otp.OtpScreenState
import com.example.students.features.auth.registration.data.model.RegistrationRequest
import com.example.students.features.auth.registration.domain.RegistrationUseCase
import com.example.students.features.otp.domain.OtpUseCase
import com.example.students.utils.GlobalPreferences
import com.example.students.utils.isSuccessful
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.Exception

class AuthViewModel constructor(
    private val loginUseCase: LoginUseCase,
    private val registrationUseCase: RegistrationUseCase,
    private val otpUseCase: OtpUseCase,
    private val prefs: GlobalPreferences,
) : ViewModel() {

    private val _state: MutableSharedFlow<OtpScreenState> =
        MutableSharedFlow()
    val state: SharedFlow<OtpScreenState> = _state


    private var authTypeField = AuthType.LOGIN
    private var passwordField: String = ""
    private var phoneNumberField: String = ""

    var codeField: String = ""

    fun auth() {
        when (authTypeField) {
            AuthType.LOGIN -> login()
            AuthType.REGISTRATION -> registration()
        }
    }

    fun getOtp() {
        viewModelScope.launch {
            _state.emit(OtpScreenState.Loading)
            try {
                val result = otpUseCase.sendOtp(phoneNumber = phoneNumberField)
                if (result.isSuccessful()) {
                    _state.emit(OtpScreenState.OtpResultSuccess)
                } else {
                    _state.emit(OtpScreenState.OtpResultFailed)
                }
            } catch (e: Exception) {
                _state.emit(
                    OtpScreenState.Exception(
                        e.localizedMessage
                            ?: "Exception during getting otp code"
                    )
                )
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.emit(OtpScreenState.Loading)
            try {
                val result = loginUseCase.login(
                    request = LoginRequest(
                        phone = phoneNumberField,
                        password = passwordField,
                        code = codeField
                    )
                )
                if (result.isSuccessful()) {
                    prefs.setUserPhone(phoneNumberField)
                    _state.emit(OtpScreenState.Success)
                    return@launch
                } else {
                    _state.emit(OtpScreenState.Failed)
                }
            } catch (e: Exception) {
                _state.emit(
                    OtpScreenState.Exception(
                        e.localizedMessage
                            ?: "Exception during login"
                    )
                )
            }
        }
    }

    private fun registration() {
        viewModelScope.launch {
            _state.emit(OtpScreenState.Loading)
            try {
                val result = registrationUseCase.registration(
                    request = RegistrationRequest(
                        phone = phoneNumberField,
                        password = passwordField,
                        code = codeField
                    )
                )
                if (result.isSuccessful()) {
                    prefs.setUserPhone(phoneNumberField)
                    _state.emit(OtpScreenState.RegistrationSuccess)
                    return@launch
                } else {
                    _state.emit(OtpScreenState.Failed)
                }
            } catch (e: Exception) {
                _state.emit(
                    OtpScreenState.Exception(
                        e.localizedMessage
                            ?: "Exception during registration"
                    )
                )
            }
        }
    }

    fun setFields(phone: String, password: String, authType: AuthType) {
        passwordField = password
        phoneNumberField = phone
        authTypeField = authType
    }

    companion object {
        const val DEFAULT_CODE = "1111"
    }
}

enum class AuthType {
    LOGIN, REGISTRATION
}