package com.example.students.features.main.feed.data.model

data class CreatePostRequest(
    val body: String,
    val description: String,
    val title: String,
    var categories_ids: List<Int>
)