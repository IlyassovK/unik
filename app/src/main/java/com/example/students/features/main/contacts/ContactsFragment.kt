package com.example.students.features.main.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.students.R
import com.example.students.databinding.FragmentContactsBinding
import com.example.students.databinding.FragmentInDevelopingBinding

class ContactsFragment : Fragment() {

    //    private lateinit var binding: FragmentContactsBinding
    private lateinit var binding: FragmentInDevelopingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
//        binding = FragmentContactsBinding.inflate(inflater, container, false)
        binding = FragmentInDevelopingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}