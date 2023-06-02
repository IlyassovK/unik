package com.example.students.features.main.feed.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.students.R
import com.example.students.databinding.FragmentPostBinding
import com.example.students.features.main.feed.presentation.model.Post

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val post: Post = requireArguments().get(ARG_POST) as Post
        with(binding) {
            crossBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            title.text = post.title
            authorName.text = post.authorName
            description.text = post.description
            body.text = post.body
        }
    }

    companion object {
        const val ARG_POST = "feed:post_argument"
    }
}