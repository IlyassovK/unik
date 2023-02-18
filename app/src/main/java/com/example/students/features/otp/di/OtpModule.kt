package com.example.students.features.otp.di

import com.example.students.features.otp.data.OtpApi
import com.example.students.features.otp.data.OtpRepository
import com.example.students.features.otp.data.OtpRepositoryImpl
import com.example.students.features.otp.domain.OtpUseCase
import org.koin.dsl.module
import retrofit2.Retrofit

val otpModule = module {
    single<OtpRepository> { OtpRepositoryImpl(preferences = get(), otpApi = get()) }
    single { get<Retrofit>().create(OtpApi::class.java) }
    single { OtpUseCase(otpRepository = get()) }
}