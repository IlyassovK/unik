package com.example.students.features.main.profile.domain.repository

import com.example.students.features.main.profile.data.ProfileApi
import com.example.students.features.main.profile.data.model.FindFriendResponse
import com.example.students.features.main.profile.data.model.FriendRequest
import com.example.students.features.main.profile.data.model.FriendsResponse
import com.example.students.features.main.profile.data.model.MeResponse
import com.example.students.features.main.profile.data.repository.ProfileRepository
import com.example.students.utils.GlobalPreferences
import com.example.students.utils.Resource
import com.example.students.utils.exceptions.NetworkExceptions
import okhttp3.MultipartBody

class ProfileRepositoryImpl(
    private val api: ProfileApi,
    private val pref: GlobalPreferences,
) : ProfileRepository {
    override suspend fun getMe(): Resource<MeResponse> {
        return try {
            val result = api.getMe()
            if (result.isSuccessful && result.body() != null) {
                return Resource.success(result.body()!!)
            } else {
                return Resource.error(NetworkExceptions.BadRequest("Operation is unsuccessful"))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during get information"))
        }
    }


    override suspend fun getFriendsRequests(): Resource<FriendsResponse> {
        return try {
            val result = api.getFriendsRequests(pref.getUserId().toInt())
            if (result.isSuccessful && result.body() != null) {
                return Resource.success(result.body()!!)
            } else {
                return Resource.error(NetworkExceptions.BadRequest("Operation is unsuccessful"))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during getting friends requests"))
        }
    }

    override suspend fun createFriendRequest(friendId: Int): Resource<FriendsResponse> {
        return try {
            val result = api.createFriendRequest(
                request = FriendRequest(
                    userId = pref.getUserId().toInt(),
                    friendId = friendId
                )
            )
            if (result.isSuccessful && result.body() != null) {
                return Resource.success(result.body()!!)
            } else {
                return Resource.error(NetworkExceptions.BadRequest("Operation is unsuccessful"))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during creating friends request"))
        }
    }

    override suspend fun getFriends(): Resource<FriendsResponse> {
        return try {
            val result = api.getFriends(pref.getUserId().toInt())
            if (result.isSuccessful && result.body() != null) {
                return Resource.success(result.body()!!)
            } else {
                return Resource.error(NetworkExceptions.BadRequest("Operation is unsuccessful"))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during getting friends"))
        }
    }

    override suspend fun addFriend(friendId: Int): Resource<FriendsResponse> {
        return try {
            val result = api.addFriend(
                request = FriendRequest(
                    userId = pref.getUserId().toInt(),
                    friendId = friendId
                )
            )
            if (result.isSuccessful && result.body() != null) {
                return Resource.success(result.body()!!)
            } else {
                return Resource.error(NetworkExceptions.BadRequest("Operation is unsuccessful"))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during getting friends"))
        }
    }

    override suspend fun findByName(name: String): Resource<FindFriendResponse> {
        return try {
            val result = api.findByName(name)
            if (result.isSuccessful && result.body() != null) {
                return Resource.success(result.body()!!)
            } else {
                return Resource.error(NetworkExceptions.BadRequest("Operation is unsuccessful"))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during finding friends by id"))
        }
    }

    override suspend fun uploadImage(part: MultipartBody.Part) {
        try {
            val result = api.uploadPicture(part)
        } catch (e: Exception) {
        }
    }
}