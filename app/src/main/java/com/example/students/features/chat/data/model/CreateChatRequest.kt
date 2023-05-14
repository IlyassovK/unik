package com.example.students.features.chat.data.model

import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

data class CreateChatRequest(
    @SerializedName("receiver")
    val receiver: Int
)

data class CreateMessageRequest(
    @SerializedName("chat_id")
    val chatId: Int,
    @SerializedName("text")
    val text: String
)

data class DialogResponse(
    @SerializedName("chat_id")
    val chatId: Int,
    @SerializedName("ownerName")
    val userName: String,
    @SerializedName("owner_avatar")
    val imgPath: String,
    @SerializedName("dialog_created_at")
    val timeCreated: String,
    @SerializedName("dialog_last_message")
    val lastMessage: Message
)

data class Dialog(
    val imgPath: String,
    val userName: String,
    val lastMessage: Message,
    val onClick: (() -> Unit) -> Unit
)
