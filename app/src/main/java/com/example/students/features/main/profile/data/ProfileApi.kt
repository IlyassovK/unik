package com.example.students.features.main.profile.data

import com.example.students.features.main.profile.data.model.FindFriendResponse
import com.example.students.features.main.profile.data.model.FriendRequest
import com.example.students.features.main.profile.data.model.FriendsResponse
import com.example.students.features.main.profile.data.model.MeResponse
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

    @GET("/api/auth/me")
    suspend fun getMe(): Response<MeResponse>

    //получить все мои запросы
    @GET("/api/friends/request/{id}/list")
    suspend fun getFriendsRequests(@Path("id") userId: Int): Response<FriendsResponse>

    //создать запрос
    @POST("/api/friends/request/create")
    suspend fun createFriendRequest(@Body request: FriendRequest): Response<FriendsResponse>

    //получить список друзей
    @GET("/api/friends/{id}/list")
    suspend fun getFriends(@Path("id") userId: Int): Response<FriendsResponse>

    //добавить друга
    @POST("/api/friends/create")
    suspend fun addFriend(@Body request: FriendRequest): Response<FriendsResponse>

    @GET("/api/user")
    suspend fun findByName(@Query("filter[name]") name: String): Response<FindFriendResponse>
}