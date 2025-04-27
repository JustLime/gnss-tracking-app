package de.hhn.gnsstrackingapp.network

import android.util.Log
import de.hhn.gnsstrackingapp.data.NtripStatus
import de.hhn.gnsstrackingapp.data.Position
import de.hhn.gnsstrackingapp.data.Precision
import de.hhn.gnsstrackingapp.data.SatelliteSystems
import de.hhn.gnsstrackingapp.data.UpdateRate
import de.hhn.gnsstrackingapp.ui.screens.settings.SettingsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class RestApiClient(private val settingsViewModel: SettingsViewModel) {
    private val client = HttpClient(Android) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun getUpdateRate(): UpdateRate {
        return try {
            val response: HttpResponse =
                client.get("http://${settingsViewModel.websocketIp.value}/rate")
            response.body()
        } catch (e: Exception) {
            Log.e("getUpdateRate", "Error fetching data: ${e.localizedMessage}", e)
            return UpdateRate(0)
        }
    }

    suspend fun getSatelliteSystems(): SatelliteSystems {
        return try {
            val response: HttpResponse =
                client.get("http://${settingsViewModel.websocketIp.value}/satsystems")
            response.body()
        } catch (e: Exception) {
            Log.e("getSatelliteSystems", "Error fetching data: ${e.localizedMessage}", e)
            return SatelliteSystems(0, 0, 0, 0)
        }
    }

    suspend fun getNtripStatus(): NtripStatus {
        return try {
            val response: HttpResponse =
                client.get("http://${settingsViewModel.websocketIp.value}/ntrip")
            response.body()
        } catch (e: Exception) {
            Log.e("getNtripStatus", "Error fetching data: ${e.localizedMessage}", e)
            return NtripStatus(false)
        }
    }

    suspend fun getPrecision(): Precision {
        return try {
            val response: HttpResponse =
                client.get("http://${settingsViewModel.websocketIp.value}/precision")
            response.body()
        } catch (e: Exception) {
            Log.e("getPrecision", "Error fetching data: ${e.localizedMessage}", e)
            return Precision("0", "0")
        }
    }

    suspend fun getPosition(): Position {
        return try {
            val response: HttpResponse =
                client.get("http://${settingsViewModel.websocketIp.value}/position")
            response.body()
        } catch (e: Exception) {
            Log.e("getPosition", "Error fetching data: ${e.localizedMessage}", e)
            Position("", "", "", "", 0)
        }
    }

    suspend fun setUpdateRate(updateRate: UpdateRate): Int {
        return try {
            val response: HttpResponse =
                client.post("http://${settingsViewModel.websocketIp.value}/rate") {
                    contentType(ContentType.Application.Json)
                    setBody(updateRate)
                }
            response.status.value
        } catch (e: Exception) {
            Log.e("setUpdateRate", "Error fetching data: ${e.localizedMessage}", e)
            400
        }
    }

    suspend fun setSatelliteSystems(satelliteSystems: SatelliteSystems): Int {
        return try {
            val response: HttpResponse =
                client.post("http://${settingsViewModel.websocketIp.value}/satsystems") {
                    contentType(ContentType.Application.Json)
                    setBody(satelliteSystems)
                }
            response.status.value
        } catch (e: Exception) {
            Log.e("setSatelliteSystems", "Error fetching data: ${e.localizedMessage}", e)
            400
        }
    }

    suspend fun setNtripStatus(ntripStatus: NtripStatus): Int {
        return try {
            val response: HttpResponse =
                client.post("http://${settingsViewModel.websocketIp.value}/ntrip") {
                    contentType(ContentType.Application.Json)
                    setBody(ntripStatus)
                }
            response.status.value
        } catch (e: Exception) {
            Log.e("setNtripStatus", "Error fetching data: ${e.localizedMessage}", e)
            400
        }
    }

    suspend fun setPrecision(precision: Precision): Int {
        return try {
            val response: HttpResponse =
                client.post("http://${settingsViewModel.websocketIp.value}/precision") {
                    contentType(ContentType.Application.Json)
                    setBody(precision)
                }
            response.status.value
        } catch (e: Exception) {
            Log.e("setPrecision", "Error fetching data: ${e.localizedMessage}", e)
            400
        }
    }
}