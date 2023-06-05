package com.example.students.features.main.profile.presentation.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.students.features.main.profile.data.model.Friend
import com.example.students.features.main.profile.data.model.FriendsResponse
import com.example.students.features.main.profile.domain.ProfileInteractor
import com.example.students.utils.isSuccessful
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class FriendsViewModel(
    private val interactor: ProfileInteractor,
) : ViewModel() {

    private val _state: MutableSharedFlow<FriendsState> =
        MutableSharedFlow()
    val state: SharedFlow<FriendsState> = _state

    private val _findState: MutableSharedFlow<FindUserState> =
        MutableSharedFlow()
    val findState: SharedFlow<FindUserState> = _findState

    private val _createRequestState: MutableSharedFlow<Boolean> =
        MutableSharedFlow()
    val createRequestState: SharedFlow<Boolean> = _createRequestState

    fun getFriends() {
        viewModelScope.launch {
            _state.emit(FriendsState.Loading)
            val result = interactor.getFriends()
            if (result.isSuccessful()) {
                _state.emit(FriendsState.Success(result.data!!.data))
            } else {
                _state.emit(FriendsState.Error)
            }
        }
    }

    fun findUserByName(name: String) {
        viewModelScope.launch {
            _findState.emit(FindUserState.Loading)
            val result = interactor.findByName(name)
            if (result.isSuccessful()) {
                _findState.emit(FindUserState.Success(result.data!!.data))
            } else {
                _findState.emit(FindUserState.Error)
            }
        }
    }

    fun createFriendRequest(id: Int) {
        viewModelScope.launch {
            val result = interactor.createFriendRequest(id)
            if (result.isSuccessful()) {
                _createRequestState.emit(true)
            } else {
                _createRequestState.emit(false)
            }
        }
    }
}

sealed class FriendsState {
    object Loading : FriendsState()
    object Error : FriendsState()
    data class Success(val data: List<FriendsResponse.Data>) : FriendsState()
}

sealed class FindUserState {
    object Loading : FindUserState()
    object Error : FindUserState()
    data class Success(val data: List<Friend>) : FindUserState()
}