package com.example.students.features.auth.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.auth.login.data.model.LoginRequest
import com.example.students.features.auth.login.domain.usecase.LoginUseCase
import com.example.students.features.otp.domain.OtpUseCase
import com.example.students.utils.isSuccessful
import com.example.students.utils.phoneNumberToRaw
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val otpUseCase: OtpUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<LoginScreenState> =
        MutableStateFlow(LoginScreenState.Loading)
    val state: StateFlow<LoginScreenState> = _state

    var phoneField = ""
        get() {
            return field.phoneNumberToRaw()
        }

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            _state.emit(LoginScreenState.Loading)
            try {
                /**
                 * хардкод, для получения ОТП и инста подставки
                 * чтобы не тратить зря платные ОТП
                 * */
                val otpResult = otpUseCase.sendOtp(phoneNumber = phone)
                if (otpResult.isSuccessful() && otpResult.data != null) {
                    Timber.d("KRM: otpResult.data ${otpResult.data}")
                    val result = loginUseCase.login(
                        request = LoginRequest(
                            phone = phone,
                            password = password,
                            otpResult.data
                        )
                    )
                    if (result.data != null) {
                        _state.emit(LoginScreenState.LoginSuccess)
                        return@launch
                    }
                }
                _state.emit(LoginScreenState.LoginFailed)
            } catch (e: Exception) {
                _state.emit(LoginScreenState.ErrorLoaded(e.localizedMessage))
            }
        }
    }
}