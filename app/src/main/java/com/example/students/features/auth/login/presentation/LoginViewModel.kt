package com.example.students.features.auth.login.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.auth.login.data.model.LoginRequest
import com.example.students.features.auth.login.domain.usecase.LoginUseCase
import com.example.students.features.auth.otp.domain.OtpUseCase
import com.example.students.utils.isSuccessful
import com.example.students.utils.phoneNumberToRaw
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val otpUseCase: OtpUseCase,
) : ViewModel() {
    private val _state: MutableSharedFlow<LoginScreenState> =
        MutableSharedFlow()
    val state: SharedFlow<LoginScreenState> = _state

    var phoneField = ""
        get() {
            return field.phoneNumberToRaw()
        }
    var passwordField: String = ""

    fun login() {
        viewModelScope.launch {
            _state.emit(LoginScreenState.Loading)
            try {
                /**
                 * хардкод, для получения ОТП и инста подставки
                 * чтобы не тратить зря платные ОТП
                 * */
                val otpResult = otpUseCase.sendOtp(phoneNumber = phoneField)
                if (otpResult.isSuccessful() && otpResult.data != null) {
                    Timber.d("KRM: otpResult.data ${otpResult.data}")
                    val result = loginUseCase.login(
                        request = LoginRequest(
                            phone = phoneField,
                            password = passwordField,
                            otpResult.data
                        )
                    )
                    if (result.isSuccessful()) {
                        _state.emit(LoginScreenState.LoginSuccess)
                        return@launch
                    } else {
                        _state.emit(LoginScreenState.LoginFailed)
                    }
                }
            } catch (e: Exception) {
                _state.emit(
                    LoginScreenState.ErrorLoaded(
                        e.localizedMessage ?: "Exception during login"
                    )
                )
            }
        }
    }
}