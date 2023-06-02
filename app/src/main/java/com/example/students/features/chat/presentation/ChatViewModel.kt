package com.example.students.features.chat.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.chat.data.model.*
import com.example.students.features.chat.domain.ChatUseCase
import com.example.students.utils.EventWrapper
import com.example.students.utils.Resource
import com.example.students.utils.isSuccessful
import kotlinx.coroutines.launch

class ChatViewModel(private val chatUseCase: ChatUseCase) : ViewModel() {

    val onDialogTappedLiveData = MutableLiveData<EventWrapper<DialogResponse>>()
    val dialogsLiveData = MutableLiveData<EventWrapper<List<Dialog>>>()
    val messageLiveData = MutableLiveData<EventWrapper<List<Message>>>()

    fun findDialogByName(name: String) {
        viewModelScope.launch {
            val result = chatUseCase.searchChats(name)
            when (result.state) {
                Resource.State.SUCCESS -> dialogsLiveData.value = EventWrapper(
                    result.data!!.map { singleDialog ->
                        WrapperChat.dialogWrapToUi(singleDialog) {
                            onDialogTappedLiveData.value = EventWrapper(singleDialog)
                        }
                    }
                )
            }
        }
    }

    fun getAllDialogs() {
        viewModelScope.launch {
            val result = chatUseCase.getChats()
            when (result.state) {
                Resource.State.SUCCESS -> dialogsLiveData.value = EventWrapper(
                    result.data!!.map { singleDialog ->
                        WrapperChat.dialogWrapToUi(singleDialog) {
                            onDialogTappedLiveData.value = EventWrapper(singleDialog)
                        }
                    }
                )
            }
        }
    }

    fun sendMessage(message: CreateMessageRequest) {
        viewModelScope.launch {
            val chatResult = chatUseCase.sendMessage(message)
            if (chatResult.isSuccessful()) {
                getAllMessages(message.chatId)
            }
        }
    }

    fun getAllMessages(id: Int) {
        viewModelScope.launch {
            val data = chatUseCase.getMessages(id).data!!

            messageLiveData.value = EventWrapper(data)
        }
    }

}