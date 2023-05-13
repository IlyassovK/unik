package com.example.students.features.chat.data.repository

import com.example.students.features.chat.data.model.CreateChatRequest
import com.example.students.features.chat.data.model.CreateMessageRequest
import com.example.students.features.chat.data.model.Message
import com.example.students.features.chat.data.service.ChatApi
import com.example.students.utils.Resource
import com.example.students.utils.exceptions.NetworkExceptions
import java.nio.channels.NetworkChannel

class ChatRepositoryImpl(private val chatApi: ChatApi) : ChatRepository {
    override suspend fun getAllChats(): Resource<List<Message>> {
        return try {
            val result = chatApi.getAllChats()
            return Resource.success(result)
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during getAllChat"))
        }
    }

    override suspend fun getAllMessages(): Resource<List<Message>> {
        return try {
            val result = chatApi.getAllMessages()
            return Resource.success(result)
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during getAllMessages"))
        }
    }

    override suspend fun createChat(createChatRequest: CreateChatRequest) {
        try {
            val result = chatApi.createChat(createChatRequest)
            Resource.success(result)
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during create chat"))
        }
    }

    override suspend fun createMessage(createMessageRequest: CreateMessageRequest) {
        try {
            val result = chatApi.createMessage(createMessageRequest)
            Resource.success(result)
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during create message"))
        }
    }
}