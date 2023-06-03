package com.example.students.features.chat.presentation.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.students.databinding.ItemMessageBinding
import com.example.students.databinding.ItemMessageMyBinding
import com.example.students.features.chat.data.model.Message
import com.example.students.features.chat.data.model.TestMessage

class MessageAdapter(

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<Message>()

    fun setItems(createDialog: List<Message>) {
        items = createDialog.toMutableList()
        notifyDataSetChanged()
    }

    fun setMessage(message: Message) {
        items.add(message)
        notifyItemInserted(items.size - 1)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return if (items[position].type!!) MY_MESSAGE
        else MESSAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            MY_MESSAGE -> return MessageViewHolderMy(
                ItemMessageMyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> return MessageViewHolder(
                ItemMessageBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = items[position]

        when (holder) {
            is MessageViewHolder -> holder.bind(data)
            is MessageViewHolderMy -> holder.bind(data)
        }
    }

    companion object {
        const val MY_MESSAGE = 1
        const val MESSAGE = 0
    }
}