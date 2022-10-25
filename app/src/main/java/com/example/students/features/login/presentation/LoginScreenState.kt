package com.example.students.features.login.presentation

open class LoginScreenState {
    object Loading: LoginScreenState()
    class ErrorLoaded(val message: String) : LoginScreenState()
    object LoginSuccess: LoginScreenState()
    object LoginFailed: LoginScreenState()
}
