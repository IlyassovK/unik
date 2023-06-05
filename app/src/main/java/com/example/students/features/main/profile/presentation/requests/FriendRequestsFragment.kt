package com.example.students.features.main.profile.presentation.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.students.databinding.FragmentFriendRequestsBinding
import com.example.students.features.main.profile.presentation.friends.adapters.FriendAdapterType
import com.example.students.features.main.profile.presentation.friends.adapters.FriendsRecyclerViewAdapter
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel


class FriendRequestsFragment : Fragment() {

    private lateinit var binding: FragmentFriendRequestsBinding
    private val viewModel: RequestsViewModel by viewModel()

    private lateinit var adapter: FriendsRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFriendRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRequests()
        initViews()
        setupObservers()
    }

    private fun initViews() = with(binding) {
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        adapter = FriendsRecyclerViewAdapter(
            adapterType = FriendAdapterType.ACTIVE_REQUEST,
            onEndIconClick = {
                viewModel.acceptRequest(it.friend.id)
            }
        )
        friendsRecyclerView.adapter = adapter
        friendsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collectLatest { state ->
                when (state) {
                    FriendRequestsState.Error -> {
                        showLoading(false)
                    }
                    FriendRequestsState.Loading -> {
                        showLoading(true)
                    }
                    is FriendRequestsState.Success -> {
                        showLoading(false)
                        if (state.data.isEmpty()) {
                            binding.emptyView.visibility = View.VISIBLE
                        } else {
                            binding.emptyView.visibility = View.GONE
                            adapter.setItems(state.data.toMutableList())
                        }
                    }
                }
            }

            viewModel.acceptRequestsState.collectLatest { isSuccess ->
                if (isSuccess) {
                    Toast.makeText(
                        requireContext(),
                        "Request accepted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error on request accepting",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showLoading(show: Boolean) = with(binding) {
        progressBar.isVisible = show
        friendsRecyclerView.isVisible = !show
    }

}