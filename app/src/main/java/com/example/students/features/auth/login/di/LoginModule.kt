package com.example.students.features.auth.login.di

import com.example.students.features.auth.AuthViewModel
import com.example.students.features.auth.login.data.repository.LoginRepositoryImpl
import com.example.students.features.auth.login.data.service.LoginApi
import com.example.students.features.auth.login.domain.repository.LoginRepository
import com.example.students.features.auth.login.domain.usecase.LoginUseCase
import com.example.students.features.auth.login.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val loginModule = module {
    viewModel { LoginViewModel(loginUseCase = get(), otpUseCase = get()) }
    viewModel {
        AuthViewModel(
            otpUseCase = get(),
            loginUseCase = get(),
            registrationUseCase = get(),
            prefs = get()
        )
    }
    single<LoginRepository> { LoginRepositoryImpl(preferences = get(), loginApi = get()) }
    single { get<Retrofit>().create(LoginApi::class.java) }
    single { LoginUseCase(loginRepository = get()) }
}