package com.example.students.features.inDev

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.students.databinding.FragmentInDevelopingBinding

class InDevelopingFragment : Fragment() {

    private lateinit var binding: FragmentInDevelopingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentInDevelopingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.toolbarTitle.text = ""
//        binding.back.setOnClickListener {
//            findNavController().popBackStack()
//        }
    }
}