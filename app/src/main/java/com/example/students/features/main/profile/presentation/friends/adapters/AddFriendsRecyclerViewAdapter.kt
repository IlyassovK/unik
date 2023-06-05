package com.example.students.features.main.profile.presentation.friends.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.students.R
import com.example.students.databinding.ItemFriendBinding
import com.example.students.features.main.profile.data.model.Friend

class AddFriendsRecyclerViewAdapter(
    private val onEndIconClick: ((Friend) -> Unit) = {},
) : RecyclerView.Adapter<AddFriendsRecyclerViewAdapter.AddFriendsViewHolder>() {

    inner class AddFriendsViewHolder(val binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    private var items = mutableListOf<Friend>()

    fun setItems(createDialog: MutableList<Friend>) {
        items = createDialog
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendsViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddFriendsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddFriendsViewHolder, position: Int) {
        val data = items[position]

        holder.binding.apply {
            name.text = data.name
            city.text = data.city.title
            university.text = data.university.title
            speciality.text = data.speciality.title

            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_empty_avatar)
                .error(R.drawable.ic_empty_avatar)
            if (!data.avatar.isNullOrBlank()) {
                Glide.with(holder.binding.root.context).load(data.avatar).apply(options)
                    .into(holder.binding.imageView)
            }

            button.setImageResource(R.drawable.icv_user_add)
            button.visibility = View.VISIBLE
            button.setOnClickListener {
                onEndIconClick.invoke(data)
            }

        }
    }
}