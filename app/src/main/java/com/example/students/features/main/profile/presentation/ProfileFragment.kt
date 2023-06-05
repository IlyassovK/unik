package com.example.students.features.main.profile.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.students.R
import com.example.students.databinding.FragmentProfileBinding
import com.example.students.features.main.profile.presentation.model.ProfileState
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel



class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMe()

        initViews()
        setupObservers()
    }

    private fun initViews() = with(binding) {
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        friendsContainer.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_friendsFragment
            )
        }

    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collectLatest {
                onState(it)
            }
        }
    }

    private fun onState(state: ProfileState) {
        when (state) {
            ProfileState.ErrorLoaded -> {
                showLoading(false)
            }
            ProfileState.Loading -> {
                showLoading(true)
            }
            is ProfileState.Success -> {
                showLoading(false)
                state.data.data.let {
                    var hobbies = ""
                    it.hobbies.forEach { hobby ->
                        hobbies += "${hobby.title} "
                    }
                    with(binding) {
                        nameEditText.setText(it.name)
                        phoneEditText.setText(it.phone)
                        emailEditText.setText(it.email)
                        cityEditText.setText(it.city.title)
                        hobbyEditText.setText(hobbies)
                        universityEditText.setText(it.university.title)
                        specialityEditText.setText(it.speciality.title)
                    }
                    setAvatar(it.avatar)
                }
            }
        }
    }

    private fun showLoading(show: Boolean) = with(binding) {
            progressBar.isVisible = show
            contentContainer.isVisible = !show
    }

    private fun setAvatar(url: String) {
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_empty_avatar)
            .error(R.drawable.ic_empty_avatar)
        if (!url.isNullOrBlank()) {
            Glide.with(requireContext()).load(url).apply(options).into(binding.avatar)
        }
    }
}