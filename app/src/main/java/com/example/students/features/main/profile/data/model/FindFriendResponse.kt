package com.example.students.features.main.profile.data.model

import com.google.gson.annotations.SerializedName

data class FindFriendResponse(
    @SerializedName("data")
    val data: List<Friend>,
)