package com.example.students.features.main.profile.domain

import com.example.students.features.main.profile.data.model.FindFriendResponse
import com.example.students.features.main.profile.data.model.FriendsResponse
import com.example.students.features.main.profile.data.model.MeResponse
import com.example.students.features.main.profile.data.repository.ProfileRepository
import com.example.students.utils.Resource
import okhttp3.MultipartBody

class ProfileInteractor(
    private val repository: ProfileRepository,
) {

    suspend fun getMe(): Resource<MeResponse> {
        return repository.getMe()
    }

    suspend fun getFriendsRequests(): Resource<FriendsResponse> {
        return repository.getFriendsRequests()
    }

    suspend fun createFriendRequest(friendId: Int): Resource<FriendsResponse> {
        return repository.createFriendRequest(friendId)
    }

    suspend fun getFriends(): Resource<FriendsResponse> {
        return repository.getFriends()
    }

    suspend fun addFriend(friendId: Int): Resource<FriendsResponse> {
        return repository.addFriend(friendId)
    }

    suspend fun findByName(name: String): Resource<FindFriendResponse> {
        return repository.findByName(name)
    }

    suspend fun uploadImage(part: MultipartBody.Part) {
        return repository.uploadImage(part)
    }

    suspend fun logout() {
        return repository.logout()
    }
}