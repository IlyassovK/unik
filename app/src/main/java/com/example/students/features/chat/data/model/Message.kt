package com.example.students.features.chat.data.model

import java.sql.Timestamp

data class Message(
    val messageId: Int,
    val messageText: String,
    val type: Boolean,
    val time: Timestamp
)
