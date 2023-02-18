package com.example.students.features.login.presentation

import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.students.R
import com.example.students.databinding.FragmentLoginBinding
import com.example.students.utils.ui.PhoneTextWatcher
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModel()

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
                    viewModel.login(
                        phone = viewModel.phoneField,
                        password = binding.passwordEditText.text.toString()
                    )
                }
            }

            goRegistration.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_login_to_registrationFragment)
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

    private fun isFieldsValid(): Boolean {
        return !binding.phoneEditText.text.isNullOrBlank() && !binding.passwordEditText.text.isNullOrBlank()
    }

    private fun onStateChange(state: LoginScreenState) {
        when (state) {
            is LoginScreenState.Loading -> {
                Log.d("KRM:", "LoginScreenState.Loading")
            }
            is LoginScreenState.LoginSuccess -> {
                Log.d("KRM:", "LoginScreenState.LoginSuccess")
                Toast.makeText(requireContext(), "Success Login", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    R.id.action_navigation_login_to_mainFragment
                )
            }
            is LoginScreenState.ErrorLoaded -> {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                Log.d("KRM:", "LoginScreenState.ErrorLoaded ${state.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.apply {
            phoneEditText.removeTextChangedListener(phoneTextWatcher)
        }
    }
}