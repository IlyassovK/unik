package com.example.students.features.chat.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.chat.data.model.CreateMessageRequest
import com.example.students.features.chat.data.model.DialogResponse
import com.example.students.features.chat.data.model.WrapperChat
import com.example.students.features.chat.domain.ChatUseCase
import com.example.students.features.chat.presentation.dialoglist.ChatState
import com.example.students.features.chat.presentation.dialoglist.DialogsState
import com.example.students.utils.EventWrapper
import com.example.students.utils.Resource
import com.example.students.utils.isSuccessful
import kotlinx.coroutines.launch

class ChatViewModel(private val chatUseCase: ChatUseCase) : ViewModel() {

    val onDialogTappedLiveData = MutableLiveData<EventWrapper<DialogResponse>>()
    val dialogsLiveData = MutableLiveData<EventWrapper<DialogsState>>()
    val messageLiveData = MutableLiveData<EventWrapper<ChatState>>()

    fun findDialogByName(name: String) {
        viewModelScope.launch {
            val result = chatUseCase.searchChats(name)
            dialogsLiveData.value = when (result.state) {
                Resource.State.SUCCESS -> EventWrapper(
                    DialogsState.Success(
                        result.data!!.map { singleDialog ->
                            WrapperChat.dialogWrapToUi(singleDialog) {
                                onDialogTappedLiveData.value = EventWrapper(singleDialog)
                            }
                        }
                    )
                )
                Resource.State.LOADING -> {
                    EventWrapper(DialogsState.Loading)
                }
                Resource.State.ERROR -> {
                    EventWrapper(DialogsState.ErrorLoaded)
                }
            }
        }
    }

    fun getAllDialogs() {
        viewModelScope.launch {
            val result = chatUseCase.getChats()
            dialogsLiveData.value = when (result.state) {
                Resource.State.SUCCESS -> EventWrapper(
                    DialogsState.Success(
                        result.data!!.map { singleDialog ->
                            WrapperChat.dialogWrapToUi(singleDialog) {
                                onDialogTappedLiveData.value = EventWrapper(singleDialog)
                            }
                        }
                    )
                )
                Resource.State.LOADING -> {
                    EventWrapper(DialogsState.Loading)
                }
                Resource.State.ERROR -> {
                    EventWrapper(DialogsState.ErrorLoaded)
                }
            }
        }
    }

    fun sendMessage(message: CreateMessageRequest) {
        viewModelScope.launch {
            val chatResult = chatUseCase.sendMessage(message)
//            if (chatResult.isSuccessful()) {
//                getAllMessages(message.chatId)
//            }
        }
    }

    fun getAllMessages(id: Int) {
        viewModelScope.launch {
            val result = chatUseCase.getMessages(id)
            messageLiveData.value = when (result.state) {
                Resource.State.LOADING -> {
                    EventWrapper(ChatState.Loading)
                }
                Resource.State.SUCCESS -> {
                    EventWrapper(ChatState.Success(result.data!!))
                }
                Resource.State.ERROR -> {
                    EventWrapper(ChatState.ErrorLoaded)
                }
            }
        }
    }

}