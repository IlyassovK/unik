package com.example.students.features.main.profile.di

import com.example.students.features.main.profile.data.ProfileApi
import com.example.students.features.main.profile.data.repository.ProfileRepository
import com.example.students.features.main.profile.domain.ProfileInteractor
import com.example.students.features.main.profile.domain.repository.ProfileRepositoryImpl
import com.example.students.features.main.profile.presentation.ProfileViewModel
import com.example.students.features.main.profile.presentation.friends.FriendsViewModel
import com.example.students.features.main.profile.presentation.requests.RequestsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val profileModule = module {
    viewModel { ProfileViewModel(interactor = get()) }
    viewModel { FriendsViewModel(interactor = get()) }
    viewModel { RequestsViewModel(interactor = get()) }

    single { ProfileInteractor(repository = get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(api = get(), pref = get()) }
    single { get<Retrofit>().create(ProfileApi::class.java) }
}