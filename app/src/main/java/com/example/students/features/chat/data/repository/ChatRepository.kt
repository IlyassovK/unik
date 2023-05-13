package com.example.students.features.chat.data.repository

import com.example.students.features.chat.data.model.CreateChatRequest
import com.example.students.features.chat.data.model.CreateMessageRequest
import com.example.students.features.chat.data.model.Message
import com.example.students.utils.Resource

interface ChatRepository {

    suspend fun getAllChats() : Resource<List<Message>>
    suspend fun getAllMessages() : Resource<List<Message>>
    suspend fun createChat(createChatRequest: CreateChatRequest)
    suspend fun createMessage(createMessageRequest: CreateMessageRequest)
}