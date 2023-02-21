package com.example.students.features.auth.otp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.students.MainActivity
import com.example.students.R
import com.example.students.databinding.FragmentOtpBinding
import com.example.students.features.auth.AuthViewModel
import com.example.students.utils.setSafeOnClickListener
import com.example.students.utils.ui.customViews.OtpView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class OtpFragment : Fragment() {

    private lateinit var binding: FragmentOtpBinding
    private var imm: InputMethodManager? = null

    private val viewModel: AuthViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imm = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                onStateChange(it)
            }
        }
    }

    private fun onStateChange(state: OtpScreenState) {
        binding.apply {
            when (state) {
                is OtpScreenState.Exception -> {}
                OtpScreenState.Failed -> {
                    continueButton.showNormal()
                    Toast.makeText(requireContext(), "Server error. Try again", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()
                }
                OtpScreenState.Loading -> {
                    continueButton.showLoading()
                }
                OtpScreenState.OtpResultFailed -> {
                    continueButton.showNormal()
                    Toast.makeText(requireContext(), "Fail on getting OTP", Toast.LENGTH_SHORT)
                        .show()
                }
                OtpScreenState.OtpResultSuccess -> {
                    viewModel.auth()
                }
                OtpScreenState.Success -> {
                    Toast.makeText(requireContext(), "Successfully completed", Toast.LENGTH_SHORT)
                        .show()

                    val intent = MainActivity.getIntent(requireContext())
                    startActivity(intent)
                }
                OtpScreenState.RegistrationSuccess -> {
                    findNavController().navigate(
                        R.id.action_otpFragment_to_navigation_form
                    )
                }
            }
        }
    }

    private fun initView() {
        binding.apply {
            otpView.listener = object : OtpView.Listener {
                override fun onStateChanged(isCompleted: Boolean) {
                    continueButton.isEnabled = isCompleted
                    if (isCompleted) {
                        val result = otpView.getCode()
                        viewModel.codeField = binding.otpView.getCode()

                        viewModel.getOtp()
                    }
                }
            }

            emptyView.setSafeOnClickListener {
                otpEt1.isEnabled = true
                otpEt1.isFocusableInTouchMode = true
                otpEt1.requestFocus()
                imm?.showSoftInput(otpEt1, InputMethodManager.SHOW_IMPLICIT)
            }

            otpEt1.requestFocus()
            imm?.showSoftInput(otpEt1, InputMethodManager.SHOW_IMPLICIT)

            otpEt1.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        otpView.deleteLastNew()
                    }
                }
                false
            }

            otpEt1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    //Char at newly inserted pos.
                    if (s.length > (start + before)) {
                        val currentChar = s[start + before]
                        if (!currentChar.toString().isNullOrEmpty()) {
                            otpView.typeNew(currentChar.toString())
                        }
                    }
                }

                override fun afterTextChanged(editable: Editable) {}
            })

            val otpParams = OtpView.OtpParams.build(OtpView.OtpType.REGISTRATION)
            otpView.setParams(otpParams)


            continueButton.setOnClickListener {
                viewModel.getOtp()
            }

        }
    }
}