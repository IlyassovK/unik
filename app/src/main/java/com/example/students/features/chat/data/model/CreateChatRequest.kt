package com.example.students.features.chat.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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

data class CreateMessageResponse(
    @SerializedName("message")
    val message: String
)

@Parcelize
data class DialogResponse(
    @SerializedName("chat_id")
    val chatId: Int,
    @SerializedName("ownerName")
    val userName: String,
    @SerializedName("owner_avatar")
    val imgPath: String?,
    @SerializedName("dialog_created_at")
    val timeCreated: String,
    @SerializedName("dialog_last_message")
    val lastMessage: Message?
) : Parcelable

data class Dialog(
    val chatId: Int,
    val imgPath: String?,
    val userName: String,
    val lastMessage: Message?,
    val onClick: ((() -> Unit) -> Unit)? = null
)

object WrapperChat {
    fun dialogWrapToUi(
        dialogResponse: DialogResponse,
        onClick: ((() -> Unit) -> Unit)
    ): Dialog =
        Dialog(
            dialogResponse.chatId,
            dialogResponse.imgPath,
            dialogResponse.userName,
            dialogResponse.lastMessage,
            onClick
        )


}