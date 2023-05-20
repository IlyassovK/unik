package com.example.students.features.main.contacts.domain

import com.example.students.features.main.contacts.data.MatchingRepository
import com.example.students.features.main.contacts.data.model.MatchingResponse
import com.example.students.utils.Resource

class MatchingUseCase(
    private val repository: MatchingRepository,
) {
    suspend fun getMatchingUser(): Resource<MatchingResponse> {
        return repository.getMatchingUser()
    }

    suspend fun likeOrDislikeUser(userId: Long, isLike: Boolean): Resource<String> {
        return repository.likeOrDislikeUser(userId, isLike)
    }
}