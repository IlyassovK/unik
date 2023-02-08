package com.example.students.features.feed.di

import com.example.students.features.feed.presentation.FeedPageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val feedPageModule = module {
    viewModel { FeedPageViewModel() }
}