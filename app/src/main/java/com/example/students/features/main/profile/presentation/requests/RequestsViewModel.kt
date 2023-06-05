package com.example.students.features.main.profile.presentation.requests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.main.profile.data.model.FriendsResponse
import com.example.students.features.main.profile.domain.ProfileInteractor
import com.example.students.utils.isSuccessful
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class RequestsViewModel(
    private val interactor: ProfileInteractor,
) : ViewModel() {
    private val _state: MutableSharedFlow<FriendRequestsState> =
        MutableSharedFlow()
    val state: SharedFlow<FriendRequestsState> = _state

    private val _acceptRequestState: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val acceptRequestsState: SharedFlow<Boolean> = _acceptRequestState

    fun getRequests() {
        viewModelScope.launch {
            _state.emit(FriendRequestsState.Loading)
            val result = interactor.getFriendsRequests()
            if (result.isSuccessful()) {
                _state.emit(FriendRequestsState.Success(result.data!!.data))
            } else {
                _state.emit(FriendRequestsState.Error)
            }
        }
    }

    fun acceptRequest(friendId: Int) {
        viewModelScope.launch {
            val result = interactor.addFriend(friendId)
            if (result.isSuccessful()) {
                _acceptRequestState.emit(true)
            } else {
                _acceptRequestState.emit(false)
            }
        }
    }
}

sealed class FriendRequestsState {
    object Loading : FriendRequestsState()
    object Error : FriendRequestsState()
    data class Success(val data: List<FriendsResponse.Data>) : FriendRequestsState()
}