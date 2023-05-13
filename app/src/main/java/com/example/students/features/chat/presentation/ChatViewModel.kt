package com.example.students.features.chat.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {

    private val _onDialogTappedLiveData: MutableLiveData<Unit> = MutableLiveData()
    val onDialogTappedLiveData: LiveData<Unit> = _onDialogTappedLiveData

    fun getAllDialogs() {

    }

}