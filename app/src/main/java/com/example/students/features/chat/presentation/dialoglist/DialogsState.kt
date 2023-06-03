package com.example.students.features.chat.presentation.dialoglist

import com.example.students.features.chat.data.model.Dialog
import com.example.students.features.chat.data.model.Message

sealed class DialogsState {
    object Loading : DialogsState()
    object ErrorLoaded : DialogsState()
    class Success(val data: List<Dialog>) : DialogsState()
}

sealed class ChatState {
    object Loading : ChatState()
    object ErrorLoaded : ChatState()
    class Success(val data: List<Message>) : ChatState()
}
