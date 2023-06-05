package com.example.students.features.main.profile.presentation.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.students.R
import com.example.students.databinding.FragmentFriendsBinding
import com.example.students.features.main.profile.presentation.friends.adapters.FriendAdapterType
import com.example.students.features.main.profile.presentation.friends.adapters.FriendsRecyclerViewAdapter
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    private val viewModel: FriendsViewModel by sharedViewModel()

    private lateinit var adapter: FriendsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFriends()
        initViews()
        setupObservers()
    }

    private fun initViews() = with(binding) {
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        requestsButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_friendsFragment_to_friendRequestsFragment
            )
        }

        addFriendBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_friendsFragment_to_addFriendDialog
            )
        }

        adapter = FriendsRecyclerViewAdapter(
            adapterType = FriendAdapterType.ALL_FRIENDS,
            onEndIconClick = {
                findNavController().navigate(
                    R.id.action_friendsFragment_to_chatFragment
                )
            }
        )
        friendsRecyclerView.adapter = adapter
        friendsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collectLatest { state ->
                when (state) {
                    FriendsState.Error -> {
                        showLoading(false)
                    }
                    FriendsState.Loading -> {
                        showLoading(true)
                    }
                    is FriendsState.Success -> {
                        showLoading(false)
                        adapter.setItems(state.data.toMutableList())
                    }
                }
            }
        }
    }

    private fun showLoading(show: Boolean) = with(binding) {
        progressBar.isVisible = show
        friendsRecyclerView.isVisible = !show
    }

}