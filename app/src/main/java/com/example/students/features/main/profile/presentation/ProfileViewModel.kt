package com.example.students.features.main.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.main.profile.domain.ProfileInteractor
import com.example.students.features.main.profile.presentation.model.ProfileState
import com.example.students.utils.isSuccessful
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.InputStream

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

    fun upload(inputStream: InputStream) {
        viewModelScope.launch {
            val part = MultipartBody.Part.createFormData(
                "pic", "myPic", RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    inputStream.readBytes()
                )
            )
            interactor.uploadImage(part)
        }
    }
}