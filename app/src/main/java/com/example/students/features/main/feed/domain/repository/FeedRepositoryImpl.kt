package com.example.students.features.main.feed.domain.repository

import com.example.students.features.main.feed.data.FeedApi
import com.example.students.features.main.feed.data.model.*
import com.example.students.features.main.feed.data.repository.FeedRepository
import com.example.students.features.main.feed.presentation.model.Category
import com.example.students.features.main.feed.presentation.model.Post
import com.example.students.utils.Resource
import com.example.students.utils.exceptions.NetworkExceptions
import retrofit2.Response

class FeedRepositoryImpl(
    private val api: FeedApi,
) : FeedRepository {
    override suspend fun getPosts(id: Int): Resource<List<Post>> {
        return try {
            val result = api.getPosts(id)
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

    override suspend fun getAllCategories(): Resource<List<Category>> {
        return try {
            val result = api.getAllCategories()
            if (result.isSuccessful && result.body() != null) {
                return Resource.success(result.body()!!.data.map {
                    it.toUiCategory()
                })
            } else {
                return Resource.error(NetworkExceptions.BadRequest("Operation is unsuccessful"))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during getting categories"))
        }
    }
}