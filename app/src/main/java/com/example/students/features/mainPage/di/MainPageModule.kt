package com.example.students.features.mainPage.di

import com.example.students.features.form.presentation.FormViewModel
import com.example.students.features.mainPage.presentation.MainPageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainPageModule = module {
    viewModel { MainPageViewModel() }
}