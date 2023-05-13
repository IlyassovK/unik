package com.example.students.features.main.feed.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.students.R
import com.example.students.databinding.FragmentFeedBinding
import com.example.students.features.main.feed.presentation.model.FeedState
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FeedFragment : Fragment() {
    private val binding by viewBinding(FragmentFeedBinding::bind)
    private val viewModel: FeedPageViewModel by sharedViewModel()

    private lateinit var adapter: FeedRecyclerViewAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllPosts()

        initViews()
        setupObservers()
    }

    private fun initViews() {
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getAllPosts()
            }

            adapter = FeedRecyclerViewAdapter {}
            postsRecyclerView.adapter = adapter
            postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            createPostBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_feedFragment_to_createPostDialogFragment
                )
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->
                when (state) {
                    FeedState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Getting posts error",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.swipeRefreshLayout.isRefreshing = false

                    }
                    FeedState.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is FeedState.PostsLoaded -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        Log.d("KRM:", " posts ${state.data}")
                        adapter.setPosts(state.data)
                    }
                }
            }
        }
    }
}