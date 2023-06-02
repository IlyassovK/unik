package com.example.students.features.chat.domain

import com.example.students.features.chat.data.model.CreateMessageRequest
import com.example.students.features.chat.data.model.CreateMessageResponse
import com.example.students.features.chat.data.model.DialogResponse
import com.example.students.features.chat.data.model.Message
import com.example.students.features.chat.data.repository.ChatRepository
import com.example.students.utils.Resource
import com.example.students.utils.exceptions.NetworkExceptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ChatUseCase(
    private val repository: ChatRepository,
    private val networkDispatcher: CoroutineDispatcher,
) {

    suspend fun getChats(): Resource<List<DialogResponse>> {
        return withContext(networkDispatcher) {
            try {
                val chats = repository.getAllChats()

                if (chats.state == Resource.State.SUCCESS) {
                    return@withContext Resource.success(data = chats.data!!)
                } else {
                    return@withContext Resource.error(NetworkExceptions.BadRequest())
                }
            } catch (e: Exception) {
                return@withContext Resource.error(e, Resource.Source.SERVER)
            }
        }
    }

    suspend fun searchChats(name: String): Resource<List<DialogResponse>> {
        return withContext(networkDispatcher) {
            try {
                val chats = repository.getChatsByName(name)

                if (chats.state == Resource.State.SUCCESS) {
                    return@withContext Resource.success(data = chats.data!!)
                } else {
                    return@withContext Resource.error(NetworkExceptions.BadRequest())
                }
            } catch (e: Exception) {
                return@withContext Resource.error(e, Resource.Source.SERVER)
            }
        }
    }

    suspend fun getMessages(id: Int): Resource<List<Message>> {
        return withContext(networkDispatcher) {
            try {
                val messages = repository.getAllMessages(id)

                if (messages.state == Resource.State.SUCCESS) {
                    return@withContext Resource.success(data = messages.data!!)
                } else {
                    return@withContext Resource.error(NetworkExceptions.BadRequest())
                }
            } catch (e: Exception) {
                return@withContext Resource.error(e, Resource.Source.SERVER)
            }
        }
    }

    suspend fun sendMessage(message: CreateMessageRequest) : Resource<CreateMessageResponse> {
        return repository.createMessage(message)
    }
}