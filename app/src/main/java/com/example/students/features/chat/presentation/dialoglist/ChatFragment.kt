package com.example.students.features.chat.presentation.dialoglist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.students.R
import com.example.students.databinding.FragmentChatBinding
import com.example.students.features.chat.presentation.ChatViewModel
import com.example.students.features.chat.presentation.dialog.DialogFragment.Companion.DIALOG_INFO
import com.example.students.utils.observeEvent
import com.example.students.utils.onTextChanged
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

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
        bindViewModel()
        bindViews()
        bindDialogs()
    }

    private fun bindViews() {
        binding.nameDialogEditText.setText("")
        binding.nameDialogEditText.onTextChanged {
//            binding.nameDialogTextInputLayout.isHintEnabled = (it.isNotBlank())
        }
        binding.nameDialogTextInputLayout.setEndIconOnClickListener {
            findByName()
        }
        binding.nameDialogEditText.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                findByName()
            }
            false
        }
    }

    private fun findByName() {
        binding.nameDialogEditText.isEnabled = false
        chatViewModel.findDialogByName(binding.nameDialogEditText.text.toString())
        binding.nameDialogEditText.isEnabled = true
    }

    private fun bindDialogs() {
        chatViewModel.getAllDialogs()
        binding.recyclerViewDialogs.layoutManager = LinearLayoutManager(context)

        binding.recyclerViewDialogs.adapter = dialogAdapter
    }

    private fun bindViewModel() {
        observeEvent(chatViewModel.onDialogTappedLiveData) {
            val bundle = bundleOf(DIALOG_INFO to it)
            findNavController().navigate(R.id.action_chatFragment_to_dialogFragment, bundle)
        }
        observeEvent(chatViewModel.dialogsLiveData) {
            dialogAdapter.setItems(it)
        }
    }

}