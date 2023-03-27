package com.example.android.camera2.basic

import android.util.Log
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class WsClient(uri: URI) : WebSocketClient(uri) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        // Called when the WebSocket connection is established
        Log.d("WebSocket", "Connected")
        this.send("LOG${BuildConfig.BIUA_KEY}");
    }

    override fun onMessage(message: String?) {
        // Called when a message is received from the server
        Log.d("WebSocket", "Received message: $message")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        // Called when the WebSocket connection is closed
        Log.d("WebSocket", "Disconnected")
    }

    override fun onError(ex: Exception?) {
        // Called when an error occurs
        Log.e("WebSocket", "Error: ${ex?.message}")
    }

    override fun onWebsocketHandshakeReceivedAsClient(
        conn: WebSocket?,
        request: ClientHandshake?,
        response: ServerHandshake?
    ) {
        super.onWebsocketHandshakeReceivedAsClient(conn, request, response)
        Log.d("WebSocket", "Handshake received: ${response?.httpStatusMessage}")
    }

}
