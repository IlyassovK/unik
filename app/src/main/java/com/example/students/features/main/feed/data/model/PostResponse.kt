package com.example.students.features.main.feed.data.model

import com.example.students.features.main.contacts.data.model.User
import com.google.gson.annotations.SerializedName

class PostResponse(
    @SerializedName("amountOfComments")
    val amountOfComments: Int,
    @SerializedName("amountOfLikes")
    val amountOfLikes: Int,
    @SerializedName("authorName")
    val authorName: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("comments")
    val comments: List<CommentResponse>,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("deleted_at")
    val deletedAt: Any,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("isLiked")
    val isLiked: Boolean,
    @SerializedName("isSaved")
    val isSaved: Boolean,
    @SerializedName("liked_users")
    val likedUsers: List<User>,
    @SerializedName("user_id")
    val userId: Int,
)