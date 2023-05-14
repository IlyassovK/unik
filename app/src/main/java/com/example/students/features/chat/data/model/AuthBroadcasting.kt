package com.example.students.features.chat.data.model

import com.google.gson.annotations.SerializedName

data class AuthBroadcasting(
    val auth: String
)

data class AuthBroadcastingRequest(
    @SerializedName("channel_name")
    val channelName: String,
    @SerializedName("socket_id")
    val socketId: String
)
