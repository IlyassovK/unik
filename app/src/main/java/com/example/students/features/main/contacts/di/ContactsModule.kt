package com.example.students.features.main.contacts.di

import com.example.students.features.main.contacts.data.MatchingApi
import com.example.students.features.main.contacts.data.MatchingRepository
import com.example.students.features.main.contacts.domain.MatchingRepositoryImpl
import com.example.students.features.main.contacts.domain.MatchingUseCase
import com.example.students.features.main.contacts.presentation.MatchingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit


val matchingModule = module {
    viewModel { MatchingViewModel(useCase = get()) }
    single { MatchingUseCase(repository = get()) }
    single<MatchingRepository> { MatchingRepositoryImpl(api = get()) }
    single { get<Retrofit>().create(MatchingApi::class.java) }
}