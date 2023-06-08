package com.example.students.features.main.feed.data.model

import com.example.students.features.main.contacts.data.model.User


data class PostResponse(
    val amountOfComments: Int?,
    val amountOfLikes: Int?,
    val authorName: String?,
    val body: String?,
    val categories: List<Category>?,
    val comments: List<CommentResponse>?,
    val created_at: String?,
    val deleted_at: Any?,
    val description: String?,
    val id: Int?,
    val isLiked: Boolean?,
    val isSaved: Boolean?,
    val liked_users: List<User>?,
    val title: String?,
    val updated_at: String?,
    val user_id: Int?,
)


data class Category(
    val created_at: String?,
    val deleted_at: Any?,
    val id: Int?,
    val status: Int?,
    val title: String?,
    val updated_at: String?,
)

data class City(
    val created_at: String?,
    val id: Int?,
    val title: String?,
    val updated_at: String?,
)


data class Speciality(
    val created_at: String?,
    val id: Int?,
    val title: String?,
    val updated_at: String?,
)

data class University(
    val created_at: String?,
    val id: Int?,
    val title: String?,
    val updated_at: String?,
)