package com.example.students.features.main.profile.presentation.friends

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.students.databinding.FragmentAddFriendBinding
import com.example.students.features.main.profile.presentation.friends.adapters.AddFriendsRecyclerViewAdapter
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddFriendFragment : Fragment() {

    private lateinit var binding: FragmentAddFriendBinding
    private val viewModel: FriendsViewModel by sharedViewModel()
    private lateinit var adapter: AddFriendsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.findState.collectLatest {
                when (it) {
                    FindUserState.Error -> {
                        showProgress(false)
                    }
                    FindUserState.Loading -> {
                        binding.emptyView.isVisible = false
                        showProgress(true)
                    }
                    is FindUserState.Success -> {
                        showProgress(false)
                        if (it.data.isEmpty()) {
                            binding.emptyView.isVisible = true
                        } else {
                            binding.emptyView.isVisible = false
                            Log.d("KRM: ", "list ${it.data.toMutableList()}")
                            adapter.setItems(it.data.toMutableList())
                        }
                    }
                }
            }

            viewModel.createRequestState.collectLatest { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Request created", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error during sending request",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun initViews() = with(binding) {

        binding.nameDialogTextInputLayout.setEndIconOnClickListener {
            findByName()
        }
        binding.nameDialogEditText.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                findByName()
            }
            false
        }

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        adapter = AddFriendsRecyclerViewAdapter(
            onEndIconClick = {
                viewModel.createFriendRequest(it.id)
            }
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun findByName() {
        binding.nameDialogEditText.isEnabled = false
        viewModel.findUserByName(binding.nameDialogEditText.text.toString())
        binding.nameDialogEditText.isEnabled = true
    }

    private fun showProgress(show: Boolean) = with(binding) {
        progressBar.isVisible = show
    }
}