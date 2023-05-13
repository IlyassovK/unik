package com.example.students.features.chat.di

import com.example.students.features.chat.data.repository.ChatRepository
import com.example.students.features.chat.data.repository.ChatRepositoryImpl
import com.example.students.features.chat.data.service.ChatApi
import com.example.students.features.chat.presentation.ChatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val chatModule = module {
    viewModel { ChatViewModel() }
    single<ChatRepository> {ChatRepositoryImpl(get())}
    single {get<Retrofit>().create(ChatApi::class.java)}
}