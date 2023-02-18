package com.example.students.features.auth.registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.auth.registration.data.model.RegistrationRequest
import com.example.students.features.auth.registration.domain.RegistrationUseCase
import com.example.students.utils.phoneNumberToRaw
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class RegistrationViewModel(
    private val registrationUseCase: RegistrationUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<RegistrationScreenState> =
        MutableStateFlow(RegistrationScreenState.Loading)
    val state: StateFlow<RegistrationScreenState> = _state

    var phoneField = ""
        get() {
            return field.phoneNumberToRaw()
        }

    fun registration(phone: String, password: String) {
        viewModelScope.launch {
            _state.emit(RegistrationScreenState.Loading)
            try {
                val result = registrationUseCase.registration(
                    request = RegistrationRequest(
                        phone = phone,
                        password = password
                    )
                )
                if (result.data != null) {
                    _state.emit(RegistrationScreenState.RegistrationSuccess())
                    return@launch
                }
                _state.emit(RegistrationScreenState.RegistrationFailed())

            } catch (e: Exception) {
                _state.emit(RegistrationScreenState.ErrorLoaded(e.localizedMessage))
            }
        }

    }
}