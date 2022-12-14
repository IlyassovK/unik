package com.example.students.features.form.presentation

sealed class FormScreenState {
    object Loading : FormScreenState()
    class ErrorLoaded(val message: String) : FormScreenState()
    object Success : FormScreenState()
    object Failed : FormScreenState()
}
