package com.example.students.features.chat.data.model

import android.os.Parcelable
import com.example.students.utils.convert
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.sql.Timestamp

@Parcelize
data class Message(
    @SerializedName("message_id")
    val messageId: Int?,
    @SerializedName("message")
    val messageText: String?,
    @SerializedName("my_message")
    val type: Boolean?,
    @SerializedName("message_created_at")
    val timeCreated: Timestamp?,
    @SerializedName("message_updated_at")
    val timeUpdated: Timestamp?,
) : Parcelable

data class TestMessage(
    val message: String,
    val message_created_at: String,
    val sender_id: Int,
)

fun TestMessage.parse(): Message {
    return Message(
        messageId = null,
        messageText = this.message,
        timeCreated = this.message_created_at.convert(),
        type = (sender_id == 18),
        timeUpdated = null
    )
}