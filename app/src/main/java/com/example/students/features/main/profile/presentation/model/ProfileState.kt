package com.example.students.features.main.profile.presentation.model

import com.example.students.features.main.profile.data.model.MeResponse

sealed class ProfileState {
    object Loading : ProfileState()
    object ErrorLoaded : ProfileState()
    data class Success(val data: MeResponse) : ProfileState()
}