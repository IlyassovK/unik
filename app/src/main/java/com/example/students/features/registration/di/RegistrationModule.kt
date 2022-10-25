package com.example.students.features.registration.di

import com.example.students.features.login.data.repository.LoginRepositoryImpl
import com.example.students.features.login.data.service.LoginApi
import com.example.students.features.login.domain.repository.LoginRepository
import com.example.students.features.login.domain.usecase.LoginUseCase
import com.example.students.features.login.presentation.LoginViewModel
import com.example.students.features.registration.data.RegistrationApi
import com.example.students.features.registration.data.repository.RegistrationRepositoryImpl
import com.example.students.features.registration.domain.RegistrationUseCase
import com.example.students.features.registration.domain.repository.RegistrationRepository
import com.example.students.features.registration.presentation.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val registrationModule = module {
    viewModel { RegistrationViewModel(registrationUseCase = get()) }
    single<RegistrationRepository> { RegistrationRepositoryImpl(preferences = get(), registrationApi = get()) }
    single { get<Retrofit>().create(RegistrationApi::class.java) }
    single { RegistrationUseCase(registrationRepository = get()) }
}