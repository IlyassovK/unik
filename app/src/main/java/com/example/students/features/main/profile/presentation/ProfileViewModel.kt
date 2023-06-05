package com.example.students.features.main.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.main.profile.domain.ProfileInteractor
import com.example.students.features.main.profile.presentation.model.ProfileState
import com.example.students.utils.isSuccessful
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val interactor: ProfileInteractor,
) : ViewModel() {

    private val _state: MutableSharedFlow<ProfileState> =
        MutableSharedFlow()
    val state: SharedFlow<ProfileState> = _state

    fun getMe() {
        viewModelScope.launch {
            _state.emit(ProfileState.Loading)
            val result = interactor.getMe()
            if (result.isSuccessful()) {
                _state.emit(ProfileState.Success(result.data!!))
            } else {
                _state.emit(ProfileState.ErrorLoaded)
            }
        }
    }
}