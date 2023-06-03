package com.example.students.features.chat.presentation.dialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.students.databinding.ItemMessageBinding
import com.example.students.databinding.ItemMessageMyBinding
import com.example.students.features.chat.data.model.Message
import com.example.students.utils.parse

class MessageViewHolderMy(val binding: ItemMessageMyBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(message: Message) {
        binding.messageTextMy.text = message.messageText
        binding.messageTimeMy.text = message.timeCreated?.parse()
    }
}

class MessageViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(message: Message) {
        binding.messageText.text = message.messageText
        binding.messageTime.text = message.timeCreated?.parse()
    }
}