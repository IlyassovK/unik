package com.example.students.features.auth.registration.presentation

open class RegistrationScreenState {
    object Loading: RegistrationScreenState()
    class ErrorLoaded(val message: String) : RegistrationScreenState()
    class RegistrationSuccess: RegistrationScreenState()
    class RegistrationFailed: RegistrationScreenState()
}
