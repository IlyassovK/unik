package com.example.students.features.main.profile.data.model

import com.google.gson.annotations.SerializedName

data class FriendRequest(
    @SerializedName("user_id")
    private val userId: Int,
    @SerializedName("friend_id")
    private val friendId: Int,
)