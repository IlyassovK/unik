package com.example.students.features.otp.di

import com.example.students.features.login.data.repository.LoginRepositoryImpl
import com.example.students.features.login.data.service.LoginApi
import com.example.students.features.login.domain.repository.LoginRepository
import com.example.students.features.login.domain.usecase.LoginUseCase
import com.example.students.features.login.presentation.LoginViewModel
import com.example.students.features.otp.data.OtpApi
import com.example.students.features.otp.data.OtpRepository
import com.example.students.features.otp.data.OtpRepositoryImpl
import com.example.students.features.otp.domain.OtpUseCase
import com.example.students.features.registration.data.RegistrationApi
import com.example.students.features.registration.data.repository.RegistrationRepositoryImpl
import com.example.students.features.registration.domain.RegistrationUseCase
import com.example.students.features.registration.domain.repository.RegistrationRepository
import com.example.students.features.registration.presentation.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val otpModule = module {
    single<OtpRepository> { OtpRepositoryImpl(preferences = get(), otpApi = get()) }
    single { get<Retrofit>().create(OtpApi::class.java) }
    single { OtpUseCase(otpRepository = get()) }
}