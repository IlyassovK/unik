package com.example.students.features.chat.data.model

import com.google.gson.annotations.SerializedName

data class WebSocketResponse(
    @SerializedName("channel")
    var channel: String? = null,
    @SerializedName("event")
    var event: String? = null,
    @SerializedName("data")
    var data: String? = null
)

data class WebSocketData(
    @SerializedName("socket_id")
    var socketId: String? = null,
    @SerializedName("activity_timeout")
    var activityTimeout: Int? = null
)

