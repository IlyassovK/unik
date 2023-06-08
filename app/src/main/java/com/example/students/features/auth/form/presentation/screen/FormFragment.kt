package com.example.students.features.auth.form.presentation.screen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.students.MainActivity
import com.example.students.R
import com.example.students.databinding.FragmentFormBinding
import com.example.students.features.auth.form.data.model.ProfileRequest
import com.example.students.features.auth.form.presentation.model.FormScreenState
import com.example.students.features.auth.form.presentation.FormViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FormFragment : Fragment(R.layout.fragment_form) {
    private val binding by viewBinding(FragmentFormBinding::bind)
    private val viewModel: FormViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        viewModel.onFormDialogClose = {
            checkFormFields()
        }
        binding.apply {
            button.setOnClickListener {
                if (isFieldsValid()) {
                    viewModel.updateProfile(
                        request = ProfileRequest(
                            birthDate = birthDateEditText.text.toString(),
                            cityId = viewModel.selectedCity!!.id.toInt(),
                            email = emailEditText.text.toString(),
                            hobbiesIds = viewModel.hobbiesId,
                            name = fullnameEditText.text.toString(),
                            specialityId = viewModel.selectedSpeciality!!.id.toInt(),
                            universityId = viewModel.selectedUniversity!!.id.toInt()
                        )
                    )
                } else {
                    Toast.makeText(requireContext(), "Fields invalid or empty", Toast.LENGTH_LONG)
                        .show()
                }
            }

            university.setOnClickListener {
                viewModel.selectedBottomSheet = FormViewModel.FormBottomSheetType.UNIVERSITY
                findNavController().navigate(
                    R.id.action_navigation_form_to_formBottomSheetFragment
                )
            }
            speciality.setOnClickListener {
                viewModel.selectedBottomSheet = FormViewModel.FormBottomSheetType.SPECIALITY
                findNavController().navigate(
                    R.id.action_navigation_form_to_formBottomSheetFragment
                )
            }
            hobbies.setOnClickListener {
                viewModel.selectedBottomSheet = FormViewModel.FormBottomSheetType.HOBBY
                findNavController().navigate(
                    R.id.action_navigation_form_to_formBottomSheetFragment
                )
            }
            city.setOnClickListener {
                viewModel.selectedBottomSheet = FormViewModel.FormBottomSheetType.CITY
                findNavController().navigate(
                    R.id.action_navigation_form_to_formBottomSheetFragment
                )
            }
        }
    }

    private fun checkFormFields() {
        binding.apply {
            if (viewModel.selectedSpeciality != null) {
                speciality.text = viewModel.selectedSpeciality!!.title
                speciality.setTextColor(resources.getColor(R.color.black, null))
            }
            if (viewModel.selectedHobbies.isNotEmpty()) {
                var text = ""
                viewModel.selectedHobbies.forEach {
                    text += "${it.title} "
                }
                hobbies.text = text
                hobbies.setTextColor(resources.getColor(R.color.black, null))
            }
            if (viewModel.selectedUniversity != null) {
                university.text = viewModel.selectedUniversity!!.title
                university.setTextColor(resources.getColor(R.color.black, null))
            }
            if (viewModel.selectedCity != null) {
                city.text = viewModel.selectedCity!!.title
                city.setTextColor(resources.getColor(R.color.black, null))
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                onStateChange(it)
            }
        }
    }

    private fun onStateChange(state: FormScreenState) {
        when (state) {
            FormScreenState.DataLoaded -> {

            }
            is FormScreenState.ErrorLoaded -> {
                Toast.makeText(
                    requireContext(),
                    "Exception during form filling",
                    Toast.LENGTH_SHORT
                ).show()
            }
            FormScreenState.Failed -> {
                Toast.makeText(
                    requireContext(),
                    "Profile update failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
            FormScreenState.Loading -> {

            }
            FormScreenState.Success -> {
                val intent = MainActivity.getIntent(requireContext())
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }

    private fun isFieldsValid(): Boolean {
        return !binding.fullnameEditText.text.isNullOrBlank() &&
                !binding.emailEditText.text.isNullOrBlank() &&
                !binding.birthDateEditText.text.isNullOrBlank() &&
                viewModel.selectedCity != null &&
                viewModel.selectedUniversity != null &&
                viewModel.selectedSpeciality != null &&
                viewModel.selectedHobbies.isNotEmpty()
    }

}