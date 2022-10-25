package com.example.students.features.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.login.data.model.LoginRequest
import com.example.students.features.login.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<LoginScreenState> =
        MutableStateFlow(LoginScreenState.Loading)
    val state: StateFlow<LoginScreenState> = _state

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            _state.emit(LoginScreenState.Loading)
            try {
                val result = loginUseCase.login(
                    request = LoginRequest(
                        phone = phone,
                        password = password
                    )
                )
                if(result.data != null){
                    _state.emit(LoginScreenState.LoginSuccess)
                    return@launch
                }

                _state.emit(LoginScreenState.LoginFailed)
            } catch (e: Exception) {
                _state.emit(LoginScreenState.ErrorLoaded(e.localizedMessage))
            }
        }
    }
}