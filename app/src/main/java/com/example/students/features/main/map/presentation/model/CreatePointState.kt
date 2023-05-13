package com.example.students.features.main.map.presentation.model

sealed class CreatePointState {
    object Loading : CreatePointState()
    object PointCreated : CreatePointState()
    object Error : CreatePointState()
}