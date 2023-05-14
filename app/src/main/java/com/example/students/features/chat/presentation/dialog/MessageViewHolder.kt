package com.example.students.features.chat.presentation.dialog

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.students.databinding.ItemDialogsBinding
import com.example.students.databinding.ItemMessageBinding
import com.example.students.databinding.ItemMessageMyBinding
import com.example.students.features.chat.data.model.Message
import com.example.students.utils.parse


class MessageViewHolderMy (val binding: ItemMessageMyBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(message: Message) {
        Log.i("Raf", " We are here")

        binding.messageTextMy.text = message.messageText
        binding.messageTimeMy.text = message.timeCreated.parse()
    }
}
class MessageViewHolder (val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(message: Message) {
        Log.i("Raf", " We are here binding not my")
        binding.messageText.text = message.messageText
        binding.messageTime.text = message.timeCreated.parse()
    }
}