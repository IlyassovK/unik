package com.example.students.features.main.feed.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.students.databinding.DialogCreatePostBinding
import com.example.students.features.main.feed.data.model.CreatePostRequest
import com.example.students.features.main.feed.presentation.model.CreatePostState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CreatePostDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogCreatePostBinding
    private val viewModel: FeedPageViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupObservers()
    }

    private fun initViews() {
        binding.apply {
            closeBtn.setOnClickListener {
                dismiss()
            }

            createBtn.setOnClickListener {
                if (isFieldsValid()) {
                    viewModel.createPost(
                        request = CreatePostRequest(
                            title = binding.titleEditText.text.toString(),
                            description = binding.descEditText.text.toString(),
                            body = binding.bodyEditText.text.toString(),
                            categories_ids = listOf(viewModel.createPostCategoryId)
                        )
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Fill the fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            branchTypeInfo.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    branchTypeEnt.isChecked = false
                    viewModel.createPostCategoryId = 1
                }
            }

            branchTypeEnt.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    branchTypeInfo.isChecked = false
                    viewModel.createPostCategoryId = 2
                }
            }


        }
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.createState.collect { state ->
                when (state) {
                    CreatePostState.Error -> {
                        binding.createBtn.showNormal()
                        Toast.makeText(
                            requireContext(),
                            "Create post error",
                            Toast.LENGTH_SHORT
                        ).show()
                        dismiss()
                    }
                    CreatePostState.Loading -> {
                        binding.createBtn.showLoading()
                    }
                    CreatePostState.PostCreated -> {
                        binding.createBtn.showNormal()
                        Toast.makeText(
                            requireContext(),
                            "Post created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        dismiss()
                    }
                }
            }
        }
    }

    private fun isFieldsValid(): Boolean {
        return !binding.titleEditText.text.isNullOrEmpty() && !binding.bodyEditText.text.isNullOrEmpty() && !binding.descEditText.text.isNullOrEmpty() && viewModel.createPostCategoryId != -1
    }
}