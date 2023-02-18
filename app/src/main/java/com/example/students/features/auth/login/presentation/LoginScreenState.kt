package com.example.students.features.auth.login.presentation

open class LoginScreenState {
    object Loading: LoginScreenState()
    class ErrorLoaded(val message: String) : LoginScreenState()
    object LoginSuccess: LoginScreenState()
    object LoginFailed: LoginScreenState()
}
