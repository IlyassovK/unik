package com.example.students.features.auth.form.presentation.model

sealed class FormScreenState {
    object Loading : FormScreenState()
    class ErrorLoaded(val message: String) : FormScreenState()
    object Success : FormScreenState()
    object Failed : FormScreenState()
    object DataLoaded : FormScreenState()
}
