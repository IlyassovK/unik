package com.example.students.features.main.profile.data.repository

import com.example.students.features.main.profile.data.model.FindFriendResponse
import com.example.students.features.main.profile.data.model.FriendsResponse
import com.example.students.features.main.profile.data.model.MeResponse
import com.example.students.utils.Resource

interface ProfileRepository {
    suspend fun getMe(): Resource<MeResponse>

    suspend fun getFriendsRequests(): Resource<FriendsResponse>

    suspend fun createFriendRequest(friendId: Int): Resource<FriendsResponse>

    suspend fun getFriends(): Resource<FriendsResponse>

    suspend fun addFriend(friendId: Int): Resource<FriendsResponse>

    suspend fun findByName(name: String): Resource<FindFriendResponse>
}