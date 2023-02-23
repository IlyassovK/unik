package com.example.students.features.form.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.form.data.model.ProfileRequest
import com.example.students.features.form.domain.usecase.FormDataUseCase
import com.example.students.features.form.domain.usecase.UpdateProfileUseCase
import com.example.students.features.form.presentation.model.FormData
import com.example.students.features.form.presentation.model.FormScreenState
import com.example.students.utils.isSuccessful
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class FormViewModel(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val formDataUseCase: FormDataUseCase,
) : ViewModel() {

    private val _state: MutableSharedFlow<FormScreenState> =
        MutableSharedFlow()
    val state: SharedFlow<FormScreenState> = _state

    var onFormDialogClose: () -> Unit = {}

    init {
        getAllFormData()
    }

    var selectedBottomSheet = FormBottomSheetType.UNIVERSITY

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
    val specialities: MutableList<FormData> = mutableListOf()
    val cities: MutableList<FormData> = mutableListOf()

    fun updateProfile(request: ProfileRequest) {
        viewModelScope.launch {
            _state.emit(FormScreenState.Loading)
            try {
                val result = updateProfileUseCase.updateProfile(
                    request = request
                )
                _state.emit(FormScreenState.Success)

            } catch (e: Exception) {
                _state.emit(
                    FormScreenState.ErrorLoaded(
                        e.localizedMessage ?: "Exception on updating profile"
                    )
                )
            }
        }
    }

    fun getAllFormData() {
        viewModelScope.launch {
            _state.emit(FormScreenState.Loading)
            try {
                var isSuccess = true
                listOf(
                    launch {
                        val res = formDataUseCase.getAllUniversities()
                        if (res.isSuccessful()) {
                            universities.addAll(res.data!!)
                            return@launch
                        }
                        isSuccess = false
                    },
                    launch {
                        val res = formDataUseCase.getAllSpecialities()
                        if (res.isSuccessful()) {
                            specialities.addAll(res.data!!)
                            return@launch
                        }
                        isSuccess = false
                    },
                    launch {
                        val res = formDataUseCase.getAllHobbies()
                        if (res.isSuccessful()) {
                            hobbies.addAll(res.data!!)
                            return@launch
                        }
                        isSuccess = false
                    },
                    launch {
                        val res = formDataUseCase.getAllCities()
                        if (res.isSuccessful()) {
                            cities.addAll(res.data!!)
                            return@launch
                        }
                        isSuccess = false
                    }
                ).joinAll()

                if (isSuccess) {
                    _state.emit(FormScreenState.DataLoaded)
                } else {
                    _state.emit(FormScreenState.Failed)
                }
            } catch (e: Exception) {
                _state.emit(
                    FormScreenState.ErrorLoaded(
                        e.localizedMessage ?: "Exception on getting form data"
                    )
                )
            }
        }
    }

    enum class FormBottomSheetType {
        UNIVERSITY, HOBBY, SPECIALITY, CITY
    }

}