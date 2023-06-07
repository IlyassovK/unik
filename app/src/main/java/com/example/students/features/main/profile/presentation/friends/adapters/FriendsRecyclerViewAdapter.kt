package com.example.students.features.main.profile.presentation.friends.adapters

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.students.R
import com.example.students.databinding.ItemFriendBinding
import com.example.students.features.main.profile.data.model.FriendsResponse

class FriendsRecyclerViewAdapter(
    private val adapterType: FriendAdapterType = FriendAdapterType.ALL_FRIENDS,
    private val onEndIconClick: ((FriendsResponse.Data) -> Unit) = {},
) : RecyclerView.Adapter<FriendsRecyclerViewAdapter.FriendsViewHolder>() {

    inner class FriendsViewHolder(val binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    private var items = mutableListOf<FriendsResponse.Data>()

    fun setItems(createDialog: MutableList<FriendsResponse.Data>) {
        items = createDialog
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val data = items[position]

        holder.binding.apply {
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_empty_avatar)
                .error(R.drawable.ic_empty_avatar)
            if (!data.friend.avatar.isNullOrBlank()) {
                Log.d("KRM: ", "avatar ${data.friend.avatar}")
                Glide.with(holder.binding.root.context).load(data.friend.avatar).apply(options)
                    .into(holder.binding.imageView)
            }

            when (adapterType) {
                FriendAdapterType.ACTIVE_REQUEST -> {
                    button.visibility = View.VISIBLE
                    button.setOnClickListener {
                        onEndIconClick.invoke(data)

                        items.remove(data)
                        setItems(items)
                    }

                    name.text = data.user.name
                    city.text = data.user.city.title
                    university.text = data.user.university.title
                    speciality.text = data.user.speciality.title
                }
                FriendAdapterType.ALL_FRIENDS -> {
                    button.visibility = View.VISIBLE
                    button.setImageResource(R.drawable.icv_chat)
                    button.setOnClickListener {
                        onEndIconClick.invoke(data)
                    }

                    name.text = data.friend.name
                    city.text = data.friend.city.title
                    university.text = data.friend.university.title
                    speciality.text = data.friend.speciality.title
                }
            }
        }
    }
}

enum class FriendAdapterType {
    ACTIVE_REQUEST, ALL_FRIENDS
}