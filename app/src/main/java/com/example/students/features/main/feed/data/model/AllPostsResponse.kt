package com.example.students.features.main.feed.data.model

import com.example.students.features.main.feed.presentation.model.Post
import com.google.gson.annotations.SerializedName

data class AllPostsResponse(
    @SerializedName("data")
    val data: List<PostResponse>,
)

object Converter {
    fun convert2PostList(web: AllPostsResponse): List<Post> {
        return web.data.map {
            convert2Post(it)
        }
    }

    fun convert2Post(web: PostResponse) = Post(
        amountOfComments = web.amountOfComments,
        amountOfLikes = web.amountOfLikes,
        authorName = web.authorName,
        body = web.body,
        comments = web.comments,
        createdAt = web.createdAt,
        description = web.description,
        id = web.id,
        title = web.title,
        updatedAt = web.updatedAt,
        isLiked = web.isLiked,
        isSaved = web.isSaved,
        likedUsers = web.likedUsers,
        userId = web.userId
    )
}
