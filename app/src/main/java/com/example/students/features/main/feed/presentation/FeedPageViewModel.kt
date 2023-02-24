package com.example.students.features.main.feed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.main.feed.data.model.CreatePostRequest
import com.example.students.features.main.feed.domain.usecase.FeedPageUseCase
import com.example.students.features.main.feed.presentation.model.CreatePostState
import com.example.students.features.main.feed.presentation.model.FeedState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeedPageViewModel(
    private val feedUseCase: FeedPageUseCase,
) : ViewModel() {

    private val _createState: MutableSharedFlow<CreatePostState> =
        MutableSharedFlow()
    val createState: SharedFlow<CreatePostState> = _createState

    private val _state: MutableSharedFlow<FeedState> =
        MutableSharedFlow()
    val state: SharedFlow<FeedState> = _state

    fun getAllPosts() {
        viewModelScope.launch {
            try {
                _state.emit(FeedState.Loading)
                withContext(Dispatchers.IO) {
                    val result = feedUseCase.getAllPosts()
                    if (result.data != null) {
                        _state.emit(
                            FeedState.PostsLoaded(
                                data = result.data
                            )
                        )
                    } else {
                        _state.emit(FeedState.Error)
                    }
                }
            } catch (e: Exception) {
                _state.emit(FeedState.Error)
            }
        }
    }

    fun createPost(request: CreatePostRequest) {
        viewModelScope.launch {
            try {
                _createState.emit(CreatePostState.Loading)
                withContext(Dispatchers.IO) {
                    val result = feedUseCase.createPost(
                        request
                    )
                    if (result.data != null) {
                        _createState.emit(CreatePostState.PostCreated)
                    } else {
                        _createState.emit(CreatePostState.Error)
                    }
                }
            } catch (e: Exception) {
                _createState.emit(CreatePostState.Error)
            }
        }
    }
}