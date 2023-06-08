package com.example.students.features.main.feed.data.repository

import com.example.students.features.main.feed.data.model.CreatePostRequest
import com.example.students.features.main.feed.data.model.CreatePostResponse
import com.example.students.features.main.feed.data.model.LikeResponse
import com.example.students.features.main.feed.presentation.model.Category
import com.example.students.features.main.feed.presentation.model.Post
import com.example.students.utils.Resource

interface FeedRepository {
    suspend fun getPosts(id: Int): Resource<List<Post>>

    suspend fun getPostById(id: String): Resource<Post>

    suspend fun createPost(request: CreatePostRequest): Resource<CreatePostResponse>

    suspend fun likePost(id: String): Resource<LikeResponse>

    suspend fun getAllCategories(): Resource<List<Category>>

    suspend fun saveDeviceToken(token: String)
}