package com.example.students.features.main.feed.data

import com.example.students.features.main.feed.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface FeedApi {
    @GET("/api/post/list")
    suspend fun getAllPosts(): Response<AllPostsResponse>

    @GET("/api/post/list/{id}")
    suspend fun getPostById(@Path("id") id: String): Response<PostResponse>

    @POST("/api/post/create")
    suspend fun createPost(@Body request: CreatePostRequest): Response<CreatePostResponse>

    @PATCH("api/post/like/{id}")
    suspend fun likePost(@Path("id") id: String): Response<LikeResponse>
}