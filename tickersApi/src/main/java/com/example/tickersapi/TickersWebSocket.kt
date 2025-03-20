package com.example.tickersapi

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import javax.inject.Inject

class TickersWebSocket@Inject constructor(
    private val client: OkHttpClient,
    private val apiUrl: String
) {
    private val _tickerUpdates =
        MutableSharedFlow<TickersWebSocketData?>(replay = 1, extraBufferCapacity = 100)
    val tickerUpdates: SharedFlow<TickersWebSocketData?> get() = _tickerUpdates

    private var webSocket: WebSocket? = null
    var isConnected = false

    fun connect() {
        if (isConnected) return

        val request = Request.Builder()
            .url("${apiUrl}token=cuobpm9r01qve8psc3f0cuobpm9r01qve8psc3fg")
            .build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                isConnected = true
                Log.i("WEBSOCKET", "WebSocket opened successfully.")

            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.i("WEBSOCKET", "Received message: $text")
                val update = parseStockQuote(text)
                if (update != null) {
                    _tickerUpdates.tryEmit(update)
                } else {
                    Log.e("WEBSOCKET", "Failed to parse stock quote.")
                }

            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                isConnected = false
                Log.i("WEBSOCKET", "WebSocket failure: ${t.message}")
                reconnect()
            }
        })
    }


    private fun parseStockQuote(jsonString: String): TickersWebSocketData? {
        return try {
            val jsonObject = JSONObject(jsonString)
            val dataArray = jsonObject.getJSONArray("data")

            if (dataArray.length() > 0) {
                val item = dataArray.getJSONObject(0)
                TickersWebSocketData(
                    p = item.getDouble("p"),
                    s = item.getString("s"),
                    v = item.getInt("v"),
                    t = item.getLong("t"),
                )
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
        val message = """{"type":"subscribe","symbol":"$symbol"}"""
        webSocket?.send(message)
        Log.i("WEBSOCKET", "Subscribed to $symbol")
    }

}