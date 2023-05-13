package com.example.students.features.main.map.di

import com.example.students.features.main.feed.data.FeedApi
import com.example.students.features.main.feed.data.repository.FeedRepository
import com.example.students.features.main.feed.domain.repository.FeedRepositoryImpl
import com.example.students.features.main.feed.domain.usecase.FeedPageUseCase
import com.example.students.features.main.feed.presentation.FeedPageViewModel
import com.example.students.features.main.map.data.LocationRepository
import com.example.students.features.main.map.data.LocationsApi
import com.example.students.features.main.map.domain.LocationRepositoryImpl
import com.example.students.features.main.map.domain.LocationUseCase
import com.example.students.features.main.map.presentation.MapViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val mapModule = module {
    viewModel { MapViewModel(locationUseCase = get()) }
    single { LocationUseCase(repository = get(), Dispatchers.IO) }
    single<LocationRepository> { LocationRepositoryImpl(api = get()) }
    single { get<Retrofit>().create(LocationsApi::class.java) }
}