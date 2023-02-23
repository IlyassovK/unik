package com.example.students.features.auth.login.presentation

import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.students.MainActivity
import com.example.students.R
import com.example.students.databinding.FragmentLoginBinding
import com.example.students.features.auth.AuthType
import com.example.students.features.auth.AuthViewModel
import com.example.students.utils.ui.PhoneTextWatcher
import kotlinx.coroutines.MainScope
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModel()

    private val authViewModel: AuthViewModel by sharedViewModel()

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
                    viewModel.passwordField = binding.passwordEditText.text.toString()
                    viewModel.login()

//                    authViewModel.setFields(
//                        phone = viewModel.phoneField,
//                        password = viewModel.password,
//                        authType = AuthType.LOGIN
//                    )
//                    findNavController().navigate(
//                        R.id.action_navigation_login_to_otpFragment
//                    )
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
                binding.button.showLoading()
            }
            is LoginScreenState.LoginSuccess -> {
                binding.button.showNormal()

                Toast.makeText(requireContext(), "Success Login", Toast.LENGTH_SHORT).show()

                val intent = MainActivity.getIntent(requireContext())
                startActivity(intent)
            }
            is LoginScreenState.ErrorLoaded -> {
                binding.button.showNormal()
                Toast.makeText(requireContext(), "Some exception during login", Toast.LENGTH_SHORT).show()
            }
            is LoginScreenState.LoginFailed -> {
                binding.button.showNormal()
                Toast.makeText(requireContext(), "Invalid data", Toast.LENGTH_SHORT).show()
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