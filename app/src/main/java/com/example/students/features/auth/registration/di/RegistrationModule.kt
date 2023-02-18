package com.example.students.features.auth.registration.di

import com.example.students.features.auth.registration.data.RegistrationApi
import com.example.students.features.auth.registration.data.repository.RegistrationRepositoryImpl
import com.example.students.features.auth.registration.domain.RegistrationUseCase
import com.example.students.features.auth.registration.domain.repository.RegistrationRepository
import com.example.students.features.auth.registration.presentation.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val registrationModule = module {
    viewModel { RegistrationViewModel(registrationUseCase = get()) }
    single<RegistrationRepository> { RegistrationRepositoryImpl(preferences = get(), registrationApi = get()) }
    single { get<Retrofit>().create(RegistrationApi::class.java) }
    single { RegistrationUseCase(registrationRepository = get()) }
}