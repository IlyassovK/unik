package com.example.students.features.main.feed.domain.repository

import com.example.students.features.main.feed.data.FeedApi
import com.example.students.features.main.feed.data.model.*
import com.example.students.features.main.feed.data.repository.FeedRepository
import com.example.students.features.main.feed.presentation.model.Post
import com.example.students.utils.Resource
import com.example.students.utils.exceptions.NetworkExceptions

class FeedRepositoryImpl(
    private val api: FeedApi,
) : FeedRepository {
    override suspend fun getAllPosts(): Resource<List<Post>> {
        return try {
            val result = api.getAllPosts()
            if (result.isSuccessful && result.body() != null) {
                return Resource.success(Converter.convert2PostList(result.body()!!))
            } else {
                return Resource.error(NetworkExceptions.BadRequest("Operation is unsuccessful"))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during get all post"))
        }
    }

    override suspend fun getPostById(id: String): Resource<Post> {
        return try {
            val result = api.getPostById(id)
            if (result.isSuccessful && result.body() != null) {
                return Resource.success(Converter.convert2Post(result.body()!!))
            } else {
                return Resource.error(NetworkExceptions.BadRequest("Operation is unsuccessful"))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during getting post by id"))
        }
    }

    override suspend fun createPost(request: CreatePostRequest): Resource<CreatePostResponse> {
        return try {
            val result = api.createPost(request)
            if (result.isSuccessful && result.body() != null) {
                return Resource.success(result.body()!!)
            } else {
                return Resource.error(NetworkExceptions.BadRequest("Operation is unsuccessful"))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during create post"))
        }
    }

    override suspend fun likePost(id: String): Resource<LikeResponse> {
        return try {
            val result = api.likePost(id)
            if (result.isSuccessful && result.body() != null) {
                return Resource.success(result.body()!!)
            } else {
                return Resource.error(NetworkExceptions.BadRequest("Operation is unsuccessful"))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during liking post"))
        }
    }
}