package com.example.students.features.chat.presentation

import android.util.Log
import com.example.students.features.chat.data.model.*
import com.example.students.utils.removeQuotesAndUnescape
import com.google.gson.Gson
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListenerDialog(val onMessage: (message: Message) -> Unit) : WebSocketListener() {

    private val TAG = "Test"

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)

        webSocket.send(
            "{\n" +
                    "   \"event\":\"pusher:subscribe\",\n" +
                    "   \"data\":{\n" +
                    "      \"channel\":\"chatMessage\"\n" +
                    "   }\n" +
                    "}"
        )
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("Raf1", "text $text")
        val texts = text.removeQuotesAndUnescape()
        val response = Gson().fromJson(text, WebSocketResponse::class.java)
        if (response.channel.equals("chatMessage") && response.data != null) {
            val message = Gson().fromJson(response.data, TestMessage::class.java)
            onMessage.invoke(message.parse())
            Log.d(TAG + "MESSAGE", "onMessage: $texts")
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d(TAG, "onClosing: $code $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d(TAG, "onClosed: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(TAG, "onFailure: ${t.message} $response")
        super.onFailure(webSocket, t, response)
    }
}