package com.example.students.features.chat.presentation.dialoglist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.students.databinding.ItemDialogsBinding
import com.example.students.features.chat.data.model.Dialog
import com.example.students.utils.parse
import com.example.students.utils.setImage
import com.example.students.utils.setSafeOnClickListener

class DialogsAdapter(
//    private val onClick: (item: Dialog) -> Unit
) : RecyclerView.Adapter<DialogsViewHolder>() {

    private var items = emptyList<Dialog>()

    fun setItems(createDialog: List<Dialog>) {
        items = createDialog
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogsViewHolder {
        val binding = ItemDialogsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DialogsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DialogsViewHolder, position: Int) {
        val data = items[position]

        holder.binding.apply {
            root.setSafeOnClickListener {
                data.onClick.invoke {  }
            }

            dialogAvatar.setImage(data.imgPath)
            dialogUserName.text = data.userName
            dialogLastMessage.text = data.lastMessage.messageText
            dialogLastMessageTime.text = data.lastMessage.timeCreated.parse()
        }
    }
}