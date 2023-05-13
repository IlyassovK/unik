package com.example.students.features.chat.data.model

import com.google.gson.annotations.SerializedName

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

data class Dialog(
    val imgPath: String,
    val userName: String,
    val lastMessage: Message
)
