package com.example.students.features.main.feed.domain.usecase

import com.example.students.features.main.feed.data.model.CreatePostRequest
import com.example.students.features.main.feed.data.model.CreatePostResponse
import com.example.students.features.main.feed.data.model.LikeResponse
import com.example.students.features.main.feed.data.repository.FeedRepository
import com.example.students.features.main.feed.presentation.model.Category
import com.example.students.features.main.feed.presentation.model.Post
import com.example.students.utils.Resource

class FeedPageUseCase(
    private val repository: FeedRepository,
) {

    suspend fun getPosts(id: Int): Resource<List<Post>> {
        return repository.getPosts(id)
    }

    suspend fun getPostById(id: String): Resource<Post> {
        return repository.getPostById(id)
    }


    suspend fun createPost(request: CreatePostRequest): Resource<CreatePostResponse> {
        return repository.createPost(request)
    }

    suspend fun likePost(id: String): Resource<LikeResponse>{
        return repository.likePost(id)
    }

    suspend fun getAllCategories(): Resource<List<Category>>{
        return repository.getAllCategories()
    }

}