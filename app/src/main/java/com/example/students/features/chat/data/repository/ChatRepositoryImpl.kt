package com.example.students.features.chat.data.repository

import com.example.students.features.chat.data.model.*
import com.example.students.features.chat.data.service.ChatApi
import com.example.students.utils.Resource
import com.example.students.utils.exceptions.NetworkExceptions

class ChatRepositoryImpl(private val chatApi: ChatApi) : ChatRepository {
    override suspend fun getAllChats(): Resource<List<DialogResponse>> {
        return try {
            val result = chatApi.getAllChats()
            return Resource.success(result)
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during getAllChat"))
        }
    }

    override suspend fun getChatsByName(name: String): Resource<List<DialogResponse>> {
        return try {
            val result = chatApi.getChatsByName(name)
            return Resource.success(result)
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during searching chat"))
        }
    }

    override suspend fun authenticationToBroadcast(authBroadcastingRequest: AuthBroadcastingRequest): Resource<AuthBroadcasting> {
        return try {
            val result = chatApi.authBroadcast(authBroadcastingRequest)
            return Resource.success(result)
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during authentication to broadcast"))
        }
    }

    override suspend fun getAllMessages(id: Int): Resource<List<Message>> {
        return try {
            val result = chatApi.getAllMessages(id)
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

    override suspend fun createMessage(createMessageRequest: CreateMessageRequest): Resource<CreateMessageResponse> {
        return try {
            val result = chatApi.createMessage(createMessageRequest)
            Resource.success(result)
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during create message"))
        }
    }
}