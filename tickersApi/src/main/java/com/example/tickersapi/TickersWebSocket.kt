package com.example.tickersapi

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class TickersWebSocket(
    private val client: OkHttpClient,
    private val apiUrl: String
) {
    private val _tickerUpdates =
        MutableSharedFlow<StockQuote?>(replay = 1, extraBufferCapacity = 100)
    val tickerUpdates: SharedFlow<StockQuote?> = _tickerUpdates.asSharedFlow()

    private var webSocket: WebSocket? = null
    var isConnected = false

    fun connect() {
        if (isConnected) return

        val request = Request.Builder()
            .url("$apiUrl?token=cuobpm9r01qve8psc3f0cuobpm9r01qve8psc3fg")
            .build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                isConnected = true
                Log.i("WEBSOCKET", "WebSocket opened successfully.")

            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.i("WEBSOCKET", "Received message: $text")
                val update = parseStockQuote(text)
                _tickerUpdates.tryEmit(update)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                isConnected = false
                Log.i("WEBSOCKET", "WebSocket failure: ${t.message}")
                reconnect()
            }
        })
    }

    private fun parseStockQuote(jsonString: String): StockQuote? {
        return try {
            val jsonObject = JSONObject(jsonString)

            StockQuote(
                c = jsonObject.getDouble("c"),
                d = jsonObject.getDouble("d"),
                dp = jsonObject.getDouble("dp"),
                h = jsonObject.getDouble("h"),
                l = jsonObject.getDouble("l"),
                o = jsonObject.getDouble("o"),
                pc = jsonObject.getDouble("pc"),
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun reconnect() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(15000)
            connect()
        }
    }

    fun subscribeToTicker(symbol: String) {
        if (!isConnected) return
        val message = "{\"type\": \"subscribe\", \"symbol\": \"$symbol\"}"
        webSocket?.send(message)
        Log.i("WEBSOCKET", "Subscribed to $symbol")
    }


    fun unsubscribeFromTicker(symbol: String) {
        if (!isConnected) return
        val message = "{\"type\": \"unsubscribe\", \"symbol\": \"$symbol\"}"

        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(1000, "Закрываем соединение")
        isConnected = false
    }
}