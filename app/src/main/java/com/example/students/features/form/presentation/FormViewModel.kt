package com.example.students.features.form.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.form.data.model.ProfileRequest
import com.example.students.features.form.domain.usecase.UpdateProfileUseCase
import com.example.students.features.login.data.model.LoginRequest
import com.example.students.features.login.presentation.LoginScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class FormViewModel(
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<FormScreenState> =
        MutableStateFlow(FormScreenState.Loading)
    val state: StateFlow<FormScreenState> = _state

    fun updateProfile(request: ProfileInfo) {
        viewModelScope.launch {
            _state.emit(FormScreenState.Loading)
            try {
                val result = updateProfileUseCase.updateProfile(
                    request = request.convert2ProfileRequest()
                )
                _state.emit(FormScreenState.Success)

                _state.emit(FormScreenState.Failed)
            } catch (e: Exception) {
                _state.emit(FormScreenState.ErrorLoaded(e.localizedMessage))
            }
        }
    }

}