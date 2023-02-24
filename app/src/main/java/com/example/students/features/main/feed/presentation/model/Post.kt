package com.example.students.features.main.feed.presentation.model

import com.example.students.features.main.feed.data.model.CommentResponse

data class Post(
    val amountOfComments: Int,
    val amountOfLikes: Int,
    val authorName: String,
    val body: String,
    val comments: List<CommentResponse>,
    val createdAt: String,
    val description: String,
    val id: Int,
    val title: String,
    val updatedAt: String,
)