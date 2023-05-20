package com.example.students.features.main.contacts.data

import com.example.students.features.main.contacts.data.model.MatchingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MatchingApi {

    @GET("/api/tinder/matching")
    suspend fun getMatchingUser(): Response<MatchingResponse>

    @GET("/api/tinder/likeOrDislike/{userId}/{likeOrDislike}")
    suspend fun likeOrDislikeUser(
        @Path("userId") userId: Long,
        @Path("likeOrDislike") likeOrDislike: Int,
    ): Response<String>
}