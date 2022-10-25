package com.example.students.features.login.di

import com.example.students.features.login.data.repository.LoginRepositoryImpl
import com.example.students.features.login.data.service.LoginApi
import com.example.students.features.login.domain.repository.LoginRepository
import com.example.students.features.login.domain.usecase.LoginUseCase
import com.example.students.features.login.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val loginModule = module {
    viewModel { LoginViewModel(loginUseCase = get()) }
    single<LoginRepository> { LoginRepositoryImpl(preferences = get(), loginApi = get()) }
    single { get<Retrofit>().create(LoginApi::class.java) }
    single{ LoginUseCase(loginRepository = get()) }
}