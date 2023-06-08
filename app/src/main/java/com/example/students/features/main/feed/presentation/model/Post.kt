package com.example.students.features.main.feed.presentation.model

import android.os.Parcelable
import com.example.students.features.main.contacts.data.model.User
import com.example.students.features.main.feed.data.model.CommentResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val amountOfComments: Int,
    val amountOfLikes: Int,
    val authorName: String,
    val body: String,
    val comments: List<CommentResponse>,
    val description: String,
    val id: Int,
    val title: String,
    val isLiked: Boolean,
    val isSaved: Boolean,
    val likedUsers: List<User>,
    val userId: Int,
) : Parcelable