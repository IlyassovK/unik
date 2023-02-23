package com.example.students.features.auth.form.di

import com.example.students.features.auth.form.data.repository.FormRepositoryImpl
import com.example.students.features.auth.form.data.service.FormApi
import com.example.students.features.auth.form.domain.repository.FormRepository
import com.example.students.features.auth.form.domain.usecase.FormDataUseCase
import com.example.students.features.auth.form.domain.usecase.UpdateProfileUseCase
import com.example.students.features.auth.form.presentation.FormViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val formModule = module {
    viewModel { FormViewModel(updateProfileUseCase = get(), formDataUseCase = get()) }
    single<FormRepository> { FormRepositoryImpl(preference = get(), formApi = get()) }
    single { get<Retrofit>().create(FormApi::class.java) }
    single { UpdateProfileUseCase(formRepository = get()) }
    single { FormDataUseCase(formRepository = get()) }
}