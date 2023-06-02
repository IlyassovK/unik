package com.example.students.features.chat.presentation

import android.util.Log
import com.example.students.features.chat.data.model.WebSocketData
import com.example.students.features.chat.data.model.WebSocketResponse
import com.example.students.utils.removeQuotesAndUnescape
import com.google.gson.Gson
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListenerDialog : WebSocketListener() {

    private val TAG = "Test"

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.d(TAG, "onOpen:")
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
        val texts = text.removeQuotesAndUnescape()
//        val response = Gson().fromJson(text, WebSocketResponse::class.java)
//        val responseData = Gson().fromJson(response.data, WebSocketData::class.java)
        Log.d(TAG, "onMessage: $texts")
//        Log.d(TAG, "onMessage: $responseData")
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