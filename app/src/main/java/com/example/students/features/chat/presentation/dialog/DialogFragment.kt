package com.example.students.features.chat.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.students.R
import com.example.students.databinding.FragmentDialogBinding
import com.example.students.features.chat.presentation.ChatViewModel
import com.example.students.utils.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DialogFragment : Fragment() {

    lateinit var binding: FragmentDialogBinding
    private val messageAdapter = MessageAdapter()
    private val chatViewModel: ChatViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindTitle()
        bindMessages()
        onBack()
    }

    private fun bindTitle() {
        binding.titleDialog
    }

    private fun bindMessages() {
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(context)

        messageAdapter.setItems(chatViewModel.getAllMessages())
        binding.recyclerViewMessages.adapter = messageAdapter

    }

    private fun onBack() {
        binding.backButton.setSafeOnClickListener {
            findNavController().navigate(R.id.action_dialogFragment_to_chatFragment)
        }
    }

}