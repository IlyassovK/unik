package com.example.students.features.chat.presentation.dialoglist

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.students.R
import com.example.students.databinding.FragmentChatBinding
import com.example.students.features.chat.presentation.ChatViewModel
import com.example.students.utils.GlobalPreferences
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.java.KoinJavaComponent

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val dialogAdapter = DialogsAdapter()
    private val chatViewModel: ChatViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindDialogs()
    }

    private fun bindDialogs() {
        binding.recyclerViewDialogs.layoutManager = LinearLayoutManager(context)

        dialogAdapter.setItems(chatViewModel.getAllDialogs())
        binding.recyclerViewDialogs.adapter = dialogAdapter
        onClick()
    }

    private fun onClick() {
        chatViewModel.onDialogTappedLiveData.observeForever {
            findNavController().navigate(R.id.action_chatFragment_to_dialogFragment)
        }
    }

}