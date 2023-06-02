package com.example.students.features.chat.data.repository

import com.example.students.features.chat.data.model.*
import com.example.students.utils.Resource

interface ChatRepository {
    suspend fun getAllChats() : Resource<List<DialogResponse>>
    suspend fun getChatsByName(name: String) : Resource<List<DialogResponse>>
    suspend fun authenticationToBroadcast(authBroadcastingRequest: AuthBroadcastingRequest) : Resource<AuthBroadcasting>
    suspend fun getAllMessages(id: Int) : Resource<List<Message>>
    suspend fun createChat(createChatRequest: CreateChatRequest)
    suspend fun createMessage(createMessageRequest: CreateMessageRequest) : Resource<CreateMessageResponse>
}