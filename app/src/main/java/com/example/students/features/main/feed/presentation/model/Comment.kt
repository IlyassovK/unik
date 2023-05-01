package com.example.students.features.main.feed.presentation.model


data class Comment(
    val body: String,
    val id: Int,
    val postId: Int,
    val status: Int,
    val userId: Int,
)