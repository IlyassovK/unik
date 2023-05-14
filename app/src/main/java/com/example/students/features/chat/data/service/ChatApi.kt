package com.example.students.features.chat.data.service

import com.example.students.features.chat.data.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {

    @GET("/api/messenger/list/chats")
    suspend fun getAllChats(): List<DialogResponse>

    @POST("/api/broadcasting/auth")
    suspend fun authBroadcast(@Body authBroadcastingRequest: AuthBroadcastingRequest): AuthBroadcasting

    @GET("/api/messenger/list/{chatid}/messages")
    suspend fun getAllMessages(@Path("chatid") id: Int): List<Message>

    @POST("/api/messenger/create/chat")
    suspend fun createChat(createChatRequest: CreateChatRequest)

    @POST("/api/messenger/create/message")
    suspend fun createMessage(createMessageRequest: CreateMessageRequest)


}