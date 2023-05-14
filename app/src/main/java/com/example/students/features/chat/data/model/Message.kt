package com.example.students.features.chat.data.model

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class Message(
    @SerializedName("message_id")
    val messageId: Int,
    @SerializedName("message")
    val messageText: String,
    @SerializedName("my_message")
    val type: Boolean,
    @SerializedName("message_created_at")
    val timeCreated: Timestamp,
    @SerializedName("message_updated_at")
    val timeUpdated: Timestamp
)
