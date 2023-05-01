package com.example.students.features.main.feed.di

import com.example.students.features.auth.form.data.service.FormApi
import com.example.students.features.auth.otp.data.OtpRepository
import com.example.students.features.auth.otp.data.OtpRepositoryImpl
import com.example.students.features.main.feed.data.FeedApi
import com.example.students.features.main.feed.data.repository.FeedRepository
import com.example.students.features.main.feed.domain.repository.FeedRepositoryImpl
import com.example.students.features.main.feed.domain.usecase.FeedPageUseCase
import com.example.students.features.main.feed.presentation.FeedPageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val feedPageModule = module {
    viewModel { FeedPageViewModel(feedUseCase = get()) }
    single { FeedPageUseCase(repository = get()) }
    single<FeedRepository> { FeedRepositoryImpl(api = get()) }
    single { get<Retrofit>().create(FeedApi::class.java) }
}