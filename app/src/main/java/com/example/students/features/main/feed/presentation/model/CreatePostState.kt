package com.example.students.features.main.feed.presentation.model

sealed class CreatePostState {
    object Loading : CreatePostState()
    object PostCreated : CreatePostState()
    object Error : CreatePostState()
}