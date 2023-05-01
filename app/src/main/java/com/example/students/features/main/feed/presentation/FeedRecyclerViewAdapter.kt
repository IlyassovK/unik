package com.example.students.features.main.feed.presentation

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.students.R
import com.example.students.databinding.ItemFeedPostBinding
import com.example.students.features.main.feed.presentation.model.Post
import com.example.students.utils.setSafeOnClickListener

class FeedRecyclerViewAdapter(
    private val onItemClick: (post: Post) -> Unit,
) :
    RecyclerView.Adapter<FeedRecyclerViewAdapter.PostViewHolder>() {

    private var dataSet = emptyList<Post>()

    fun setPosts(posts: List<Post>) {
        this.dataSet = posts
        notifyDataSetChanged()
    }

    fun clear() {
        this.dataSet = emptyList<Post>()
        notifyDataSetChanged()
    }

    inner class PostViewHolder(val binding: ItemFeedPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FeedRecyclerViewAdapter.PostViewHolder {
        val binding =
            ItemFeedPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: FeedRecyclerViewAdapter.PostViewHolder, position: Int) {
        val data = dataSet[position]

        holder.binding.apply {
            root.setSafeOnClickListener {
                onItemClick(data)
            }

            title.text = data.title
            description.text = data.description
            author.text = data.authorName
            amountOfLikes.text = data.amountOfLikes.toString()
            amountOfComments.text = data.amountOfComments.toString()
        }
    }
}