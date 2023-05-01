package com.example.students.features.main.feed.presentation.model

sealed class FeedState {
    object Loading : FeedState()
    class PostsLoaded(val data: List<Post>) : FeedState()
    object Error : FeedState()
}