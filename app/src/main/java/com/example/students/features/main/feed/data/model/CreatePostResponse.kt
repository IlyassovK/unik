package com.example.students.features.main.feed.data.model

import com.google.gson.annotations.SerializedName

data class CreatePostResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("post")
    val post: Post,
) {
    data class Post(
        @SerializedName("body")
        val body: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("updated_at")
        val updatedAt: String,
    )
}