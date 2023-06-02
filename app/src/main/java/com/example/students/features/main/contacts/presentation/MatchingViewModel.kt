package com.example.students.features.main.contacts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.main.contacts.domain.MatchingUseCase
import com.example.students.features.main.contacts.presentation.model.MatchingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchingViewModel(
    private val useCase: MatchingUseCase,
) : ViewModel() {

    private val _matchingState: MutableSharedFlow<MatchingState> =
        MutableSharedFlow()
    val matchingState: SharedFlow<MatchingState> = _matchingState

    var currentMatchingUserId: Long = 0

    fun likeOrDislikeUser(isLike: Boolean) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    useCase.likeOrDislikeUser(
                        currentMatchingUserId,
                        isLike
                    )

                    getMatchingUser()
                }
            } catch (e: Exception) {

            }
        }
    }

    fun getMatchingUser() {
        viewModelScope.launch {
            try {
                _matchingState.emit(MatchingState.Loading)
                withContext(Dispatchers.IO) {
                    val result = useCase.getMatchingUser()
                    if (result.data != null) {
                        _matchingState.emit(
                            MatchingState.PersonLoaded(
                                data = result.data
                            )
                        )
                    } else {
                        _matchingState.emit(MatchingState.Error)
                    }
                }
            } catch (e: Exception) {
                _matchingState.emit(MatchingState.Error)
            }

        }

    }
}