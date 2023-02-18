package com.example.students.features.form.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.students.MainActivity
import com.example.students.R
import com.example.students.databinding.FragmentFormBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FormFragment : Fragment(R.layout.fragment_form) {
    private val binding by viewBinding(FragmentFormBinding::bind)
    private val viewModel: FormViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
//        setupObservers()
    }

    private fun setupViews() {
        binding.apply {
            button.setOnClickListener {
                if (isFieldsValid()) {
                    viewModel.updateProfile(
                        request = ProfileInfo(
                            birthDate = birthDateEditText.text.toString(),
                            city = cityEditText.text.toString(),
                            email = emailEditText.text.toString(),
                            hobbies = listOf(),
                            name = fullnameEditText.text.toString(),
                            speciality = specialtyyditText.text.toString(),
                            university = universityditText.text.toString()
                        )
                    )
                }
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
            is FormScreenState.Loading -> {
            }
            is FormScreenState.Success -> {
                Toast.makeText(requireContext(), "Success Login", Toast.LENGTH_SHORT).show()

                val intent = MainActivity.getIntent(requireContext())
                startActivity(intent)
            }
            is FormScreenState.ErrorLoaded -> {
                Toast.makeText(requireContext(), "Success Login", Toast.LENGTH_SHORT).show()
            }
            FormScreenState.Failed -> {

            }
        }
    }

    private fun isFieldsValid(): Boolean {
        return !binding.fullnameEditText.text.isNullOrBlank() && !binding.emailEditText.text.isNullOrBlank() && !binding.birthDateEditText.text.isNullOrBlank() && !binding.cityEditText.text.isNullOrBlank() && !binding.universityditText.text.isNullOrBlank() && !binding.specialtyyditText.text.isNullOrBlank()
    }

}