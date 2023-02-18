package com.example.students.features.registration.presentation

import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.students.R
import com.example.students.databinding.FragmentRegistrationBinding
import com.example.students.utils.ui.PhoneTextWatcher
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private val binding by viewBinding(FragmentRegistrationBinding::bind)
    private val viewModel: RegistrationViewModel by viewModel()

    private lateinit var phoneTextWatcher: TextWatcher

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        binding.apply {
            button.setOnClickListener {
                if (isFieldsValid()) {
                    viewModel.registration(
                        phone = viewModel.phoneField,
                        password = binding.passwordEditText.text.toString()
                    )
                }
            }

            goLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registrationFragment_to_navigation_login)
            }

            phoneTextWatcher = PhoneTextWatcher(requireContext(), phoneEditText) {
                viewModel.phoneField = it
            }
            phoneEditText.addTextChangedListener(phoneTextWatcher)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                onStateChange(it)
            }
        }
    }

    private fun onStateChange(state: RegistrationScreenState) {
        when (state) {
            is RegistrationScreenState.Loading -> {
            }
            is RegistrationScreenState.RegistrationSuccess -> {
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    R.id.action_registrationFragment_to_formFragment
                )
            }
            is RegistrationScreenState.RegistrationFailed -> {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }
            is RegistrationScreenState.ErrorLoaded -> {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isFieldsValid(): Boolean {
        val fieldsIsNotEmpty =
            !binding.phoneEditText.text.isNullOrBlank() && !binding.passwordEditText.text.isNullOrBlank() && !binding.repeatPasswordEditText.text.isNullOrBlank()

        return fieldsIsNotEmpty && binding.passwordEditText.text.toString() == binding.repeatPasswordEditText.text.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.apply {
            phoneEditText.removeTextChangedListener(phoneTextWatcher)
        }
    }

}