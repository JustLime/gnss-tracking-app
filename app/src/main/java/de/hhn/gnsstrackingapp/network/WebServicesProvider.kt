package de.hhn.gnsstrackingapp.network


import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import okio.ByteString
import okio.ByteString.Companion.toByteString

class WebServicesProvider(private val url: String) {

    private val client = HttpClient(OkHttp) {
        install(WebSockets)
    }

    var socketEventChannel: Channel<SocketUpdate> = Channel(10)

    suspend fun startSocket() {
        client.webSocket(urlString = url) {
            Log.d("WebSocket", "Connected to WebSocket: $url")

            while (isActive) {
                when (val frame = incoming.receive()) {
                    is Frame.Text -> {
                        val message = frame.readText()
                        socketEventChannel.send(SocketUpdate(text = message))
                    }

                    is Frame.Binary -> {
                        val data = frame.data
                        socketEventChannel.send(SocketUpdate(byteString = data.toByteString()))
                    }

                    else -> {
                        Log.d("WebSocket", "Received Unsupported Frame: $frame")
                    }
                }
            }
        }
    }

    fun stopSocket() {
        runBlocking {
            client.close()
            socketEventChannel.close()
            Log.d("WebSocket", "WebSocket closed.")
        }
    }
}


data class SocketUpdate(
    val text: String? = null,
    val byteString: ByteString? = null,
    val exception: Throwable? = null
)