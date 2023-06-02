package com.example.students.features.main.contacts.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.students.databinding.FragmentContactsBinding
import com.example.students.features.main.contacts.data.model.MatchingResponse
import com.example.students.features.main.contacts.presentation.model.MatchingState
import com.example.students.features.main.feed.presentation.model.CreatePostState
import org.koin.androidx.viewmodel.ext.android.viewModel

class MatchingFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding

    val viewModel: MatchingViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMatchingUser()

        initViews()
        setupObservers()
    }

    private fun initViews() = with(binding) {
        likeBtn.setOnClickListener {
            viewModel.likeOrDislikeUser(true)
        }

        skipBtn.setOnClickListener {
            viewModel.likeOrDislikeUser(false)
        }
    }

    private fun setupObservers() = with(binding) {
        lifecycleScope.launchWhenCreated {
            viewModel.matchingState.collect { state ->
                when (state) {
                    MatchingState.Error -> {
                        shimmerViewContainer.stopShimmer()
                    }
                    MatchingState.Loading -> {
                        shimmerViewContainer.startShimmer()
                        name.isVisible = false
                        city.isVisible = false
                        university.isVisible = false
                        universityDivider.isVisible = false
                        speciality.isVisible = false
                        hobbies.isVisible = false
                    }
                    is MatchingState.PersonLoaded -> {
                        shimmerViewContainer.hideShimmer()

                        viewModel.currentMatchingUserId = state.data.user?.id?.toLong() ?: 0
                        onPersonLoaded(state.data)

                    }
                }
            }
        }
    }

    private fun onPersonLoaded(data: MatchingResponse) = with(binding) {
        if (data.user == null) {
            noMatching.isVisible = true
        } else {
            noMatching.isVisible = false

            name.isVisible = data.user.name != null
            city.isVisible = data.user.city != null
            university.isVisible = data.user.university != null
            universityDivider.isVisible = university.isVisible
            speciality.isVisible = data.user.speciality != null
            hobbies.isVisible = data.user.hobbies != null

            var hobbiesText = ""

            data.user.hobbies?.forEachIndexed { index, hobby ->
                hobbiesText += if (index == data.user.hobbies.size - 1) {
                    hobby.title
                } else {
                    "${hobby.title},"
                }
            }


            name.text = data.user.name
            city.text = data.user.city?.title
            university.text = data.user.university?.title
            speciality.text = data.user.speciality?.title
            hobbies.text = hobbiesText
        }
    }
}
