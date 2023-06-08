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
        amountOfComments = web.amountOfComments ?: 0,
        amountOfLikes = web.amountOfLikes ?: 0,
        authorName = web.authorName ?: "",
        body = web.body ?: "",
        comments = web.comments ?: listOf(),
        description = web.description ?: "",
        id = web.id ?: 0,
        title = web.title ?: "",
        isLiked = web.isLiked ?: false,
        isSaved = web.isSaved ?: false,
        likedUsers = web.liked_users ?: listOf(),
        userId = web.user_id ?: 0
    )
}
