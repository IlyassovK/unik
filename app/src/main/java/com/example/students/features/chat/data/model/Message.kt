package com.example.students.features.chat.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
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
) : Parcelable
