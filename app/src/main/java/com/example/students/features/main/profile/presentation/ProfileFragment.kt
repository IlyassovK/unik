package com.example.students.features.main.profile.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.students.MainActivity
import com.example.students.R
import com.example.students.databinding.FragmentProfileBinding
import com.example.students.features.auth.AuthorizationActivity
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

    private val pickImagesLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val stream = requireContext().contentResolver.openInputStream(it)
                viewModel.upload(stream!!)
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMe()

        initViews()
        setupObservers()
    }

    private fun initViews() = with(binding) {
        friendButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_friendsFragment
            )
        }

        logout.setOnClickListener {
            viewModel.logout()
            requireActivity().finish()
            val intent = AuthorizationActivity.getIntent(requireContext())
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        localeButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_languageDialog
            )
        }

        avatarContainer.setOnClickListener {
            pickImagesLauncher.launch("image/*")
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
                    it.hobbies?.forEach { hobby ->
                        hobbies += "${hobby.title} "
                    }
                    with(binding) {
                        nameEditText.setText(it.name)
                        phoneEditText.setText(it.phone)
                        emailEditText.setText(it.email)
                        cityEditText.setText(it.city?.title)
                        hobbyEditText.setText(hobbies)
                        universityEditText.setText(it.university?.title)
                        specialityEditText.setText(it.speciality?.title)
                    }
                    setAvatar(it.avatar)
                }
            }
            ProfileState.ProfileUpdated -> {

            }
        }
    }

    private fun showLoading(show: Boolean) = with(binding) {
        progressBar.isVisible = show
        contentContainer.isVisible = !show
    }

    private fun setAvatar(url: String?) {
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_empty_avatar)
            .error(R.drawable.ic_empty_avatar)
        if (!url.isNullOrBlank()) {
            Glide.with(requireContext()).load(url).apply(options).into(binding.avatar)
        }
    }

    companion object {
        const val PICK_IMAGE = 1207
    }
}