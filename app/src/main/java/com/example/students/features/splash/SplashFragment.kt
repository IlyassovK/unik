package com.example.students.features.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.students.R
import com.example.students.databinding.FragmentSplashBinding
import com.example.students.utils.GlobalPreferences
import org.koin.android.ext.android.inject

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    private val prefs: GlobalPreferences by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (prefs.isFirstLaunch()) {
            findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
            prefs.firstStartHappened()
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_navigation_registration)
        }
    }

}