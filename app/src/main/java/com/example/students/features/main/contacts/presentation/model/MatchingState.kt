package com.example.students.features.main.contacts.presentation.model

import com.example.students.features.main.contacts.data.model.MatchingResponse

sealed class MatchingState {
    object Loading : MatchingState()
    class PersonLoaded(val data: MatchingResponse) : MatchingState()
    object Error : MatchingState()
}