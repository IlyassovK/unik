package com.example.students.features.main.contacts.domain

import com.example.students.features.main.contacts.data.MatchingApi
import com.example.students.features.main.contacts.data.MatchingRepository
import com.example.students.features.main.contacts.data.model.MatchingResponse
import com.example.students.utils.Resource
import com.example.students.utils.exceptions.NetworkExceptions

class MatchingRepositoryImpl(
    private val api: MatchingApi,
) : MatchingRepository {
    override suspend fun getMatchingUser(): Resource<MatchingResponse> {
        return try {
            val result = api.getMatchingUser()
            if (result.isSuccessful && result.body() != null) {
                return Resource.success(result.body()!!)
            } else {
                return Resource.error(NetworkExceptions.BadRequest("Operation is unsuccessful"))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during matching"))
        }
    }

    override suspend fun likeOrDislikeUser(userId: Long, isLike: Boolean): Resource<String> {
        return try {
            val likeOrDislike = if (isLike) 1 else 2
            val result = api.likeOrDislikeUser(userId, likeOrDislike)
            if (result.isSuccessful) {
                return Resource.success(result.body()!!)
            } else {
                return Resource.error(NetworkExceptions.BadRequest("Operation is unsuccessful"))
            }
        } catch (e: Exception) {
            Resource.error((NetworkExceptions.BadRequest("Exception during liking or disliking user")))
        }
    }
}