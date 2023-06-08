package com.example.students.features.main.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.auth.form.data.model.ProfileRequest
import com.example.students.features.auth.form.domain.usecase.UpdateProfileUseCase
import com.example.students.features.auth.form.presentation.FormViewModel
import com.example.students.features.auth.form.presentation.model.FormData
import com.example.students.features.main.profile.domain.ProfileInteractor
import com.example.students.features.main.profile.presentation.model.ProfileState
import com.example.students.utils.GlobalPreferences
import com.example.students.utils.isSuccessful
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.InputStream

class ProfileViewModel(
    private val interactor: ProfileInteractor,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val prefs: GlobalPreferences,
) : ViewModel() {

    private val _state: MutableSharedFlow<ProfileState> =
        MutableSharedFlow()
    val state: SharedFlow<ProfileState> = _state

    var selectedUniversity: FormData? = null
    var selectedHobbies = mutableListOf<FormData>()
    var selectedSpeciality: FormData? = null
    var selectedCity: FormData? = null
    val universities: MutableList<FormData> = mutableListOf()
    val hobbies: MutableList<FormData> = mutableListOf()
    val hobbiesId: List<Int>
        get() {
            return selectedHobbies.map { it.id.toInt() }
        }

    var selectedBottomSheet = FormViewModel.FormBottomSheetType.UNIVERSITY

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
                "image", "image", RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    inputStream.readBytes()
                )
            )
            interactor.uploadImage(part)
        }
    }

    fun updateProfile(data: ProfileRequest) {
        viewModelScope.launch {
            _state.emit(ProfileState.Loading)
            val result = updateProfileUseCase.updateProfile(data)
            if (result.isSuccessful()) {
                _state.emit(ProfileState.ProfileUpdated)
            } else {
                _state.emit(ProfileState.ErrorLoaded)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            interactor.logout()
            prefs.clear()
        }
    }
}