package com.example.students.features.chat.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.students.features.chat.data.model.Dialog
import com.example.students.features.chat.data.model.Message
import java.sql.Timestamp

class ChatViewModel : ViewModel() {

    private val _onDialogTappedLiveData: MutableLiveData<Unit> = MutableLiveData()
    val onDialogTappedLiveData: LiveData<Unit> = _onDialogTappedLiveData

    fun getAllDialogs() : List<Dialog> {

        val listDialog: MutableList<Dialog> = mutableListOf()
        val message = Message(1, "Huinya", true, Timestamp(System.currentTimeMillis()))
        val dialog = Dialog("https://i.ytimg.com/vi/PfzA1CX3aI4/maxresdefault.jpg", "Karim", message)
        listDialog.add(dialog)
        listDialog.add(dialog)
        listDialog.add(dialog)
        listDialog.add(dialog)
        listDialog.add(dialog)
        listDialog.add(dialog)

        return listDialog
    }

    fun getAllMessages() : List<Message> {
        val listDialog: MutableList<Message> = mutableListOf()
        val message = Message(1, "Huinya", true, Timestamp(System.currentTimeMillis()))
        listDialog.add(message)
        listDialog.add(message)
        listDialog.add(message)
        listDialog.add(message)
        listDialog.add(message)
        listDialog.add(message)
        listDialog.add(message)

        return listDialog
    }

}