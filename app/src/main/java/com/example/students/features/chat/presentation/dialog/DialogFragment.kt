package com.example.students.features.chat.presentation.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.students.databinding.FragmentDialogBinding
import com.example.students.di.network.BASE_API
import com.example.students.features.chat.data.model.CreateMessageRequest
import com.example.students.features.chat.data.model.DialogResponse
import com.example.students.features.chat.presentation.ChatViewModel
import com.example.students.features.chat.presentation.WebSocketListenerDialog
import com.example.students.features.chat.presentation.dialoglist.ChatState
import com.example.students.utils.observeEvent
import com.example.students.utils.setSafeOnClickListener
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.concurrent.TimeUnit


class DialogFragment : Fragment() {

    private lateinit var webSocketListener: WebSocketListenerDialog

    private var _webSocket: WebSocket? = null

    private val socketOkHttpClient = OkHttpClient.Builder()
        .connectTimeout(timeout, TimeUnit.MILLISECONDS)
        .readTimeout(timeout, TimeUnit.MILLISECONDS)
        .hostnameVerifier { _, _ -> true }
        .addInterceptor(
            HttpLoggingInterceptor()
                .setLevel(
                    HttpLoggingInterceptor.Level.NONE
                )
        )
        .build()

    lateinit var binding: FragmentDialogBinding
    private val messageAdapter = MessageAdapter()
    private val chatViewModel: ChatViewModel by sharedViewModel()
    lateinit var dialogInfo: DialogResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webSocketListener = WebSocketListenerDialog {
            requireActivity().runOnUiThread(Runnable {
                Log.d("TEST KRM", "message $it")
                messageAdapter.setMessage(it)
                binding.recyclerViewMessages.smoothScrollToPosition(messageAdapter.itemCount)
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogInfo = arguments?.getParcelable(DIALOG_INFO)!!

        bindWebSocket()
        observeLiveData()
        bindTitle()
        bindMessages()
        bindViews()
        onBack()
    }

    private fun bindWebSocket() {
        _webSocket = socketOkHttpClient.newWebSocket(
            createRequest(),
            webSocketListener
        )
    }


    private fun createRequest(): Request {
        val webSocketURL = "ws://${BASE_API}:6001/app/myapp-wbsocks"
//        val webSocketURL = "wss://s9053.fra1.piesocket.com/v3/1?api_key=s3WvPeTKvpqk1DM8NjcYxxSX8aJCtpvGQyEeOKvw&notify_self=1"

        return Request.Builder()
            .url(webSocketURL)
            .build()
    }

    private fun bindViews() {
        binding.messageDialogTextInputLayout.setEndIconOnClickListener {
            if (binding.messageDialogEditText.text.toString().isNotEmpty()) {
                val message = CreateMessageRequest(
                    dialogInfo.chatId,
                    binding.messageDialogEditText.text.toString()
                )
                binding.messageDialogEditText.text?.clear()
                chatViewModel.sendMessage(message)
                val mess = Gson().toJson(message)
                Log.i("Raf1", "$mess")
                val result = _webSocket?.send(mess) ?: false
                if (result) {
                    Log.i("Raf1", "Send message $mess")
                } else {
                    Log.i("Raf1", "Not send message")
                }
            }
        }
    }

    private fun bindTitle() {
        val dialogInfo = arguments?.getParcelable<DialogResponse>(DIALOG_INFO)!!
        binding.dialogName.text = dialogInfo.userName
        chatViewModel.getAllMessages(dialogInfo.chatId)
    }

    private fun bindMessages() {
        var layoutManager = LinearLayoutManager(context).apply {
            stackFromEnd = true
        }
        binding.recyclerViewMessages.layoutManager = layoutManager
        binding.recyclerViewMessages.adapter = messageAdapter
    }

    private fun onBack() {
        binding.backButton.setSafeOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeLiveData() {
        observeEvent(chatViewModel.messageLiveData) {
            when (it) {
                ChatState.ErrorLoaded -> {
                    showLoading(false)
                    Toast.makeText(
                        requireContext(),
                        "Error during getting messages",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                ChatState.Loading -> showLoading(true)
                is ChatState.Success -> {
                    showLoading(false)
                    messageAdapter.setItems(it.data)
                }
            }

        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.isVisible = show
    }

    companion object {
        const val DIALOG_INFO = "DIALOG_INFO"
        const val timeout = 200000L
    }


}