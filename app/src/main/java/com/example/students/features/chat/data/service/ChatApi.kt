package com.example.students.features.chat.data.service

import com.example.students.features.chat.data.model.CreateChatRequest
import com.example.students.features.chat.data.model.CreateMessageRequest
import com.example.students.features.chat.data.model.Message
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatApi {

    @GET("/api/messenger/list/chats")
    suspend fun getAllChats() : List<Message>

    @GET("/api/messenger/list/messages")
    suspend fun getAllMessages() : List<Message>

    @POST("/api/messenger/create/chat")
    suspend fun createChat(createChatRequest: CreateChatRequest)

    @POST("/api/messenger/create/message")
    suspend fun createMessage(createMessageRequest: CreateMessageRequest)



}