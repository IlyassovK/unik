package com.example.students.features.main.contacts.data

import com.example.students.features.main.contacts.data.model.MatchingResponse
import com.example.students.utils.Resource

interface MatchingRepository {
    suspend fun getMatchingUser(): Resource<MatchingResponse>
    suspend fun likeOrDislikeUser(userId: Long, isLike: Boolean): Resource<String>
}