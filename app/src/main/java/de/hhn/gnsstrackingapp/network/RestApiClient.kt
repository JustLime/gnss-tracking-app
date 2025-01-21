package de.hhn.gnsstrackingapp.network

import android.util.Log
import de.hhn.gnsstrackingapp.baseUrl
import de.hhn.gnsstrackingapp.data.NtripStatus
import de.hhn.gnsstrackingapp.data.Precision
import de.hhn.gnsstrackingapp.data.SatelliteSystems
import de.hhn.gnsstrackingapp.data.UpdateRate
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
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class RestApiClient {
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
            val response: HttpResponse = client.get("http://${baseUrl.value}/rate")
            response.body()
        } catch (e: Exception) {
            Log.e("getUpdateRate", "Error fetching data: ${e.localizedMessage}", e)
            return UpdateRate(0)
        }
    }

    suspend fun getSatelliteSystems(): SatelliteSystems {
        return try {
            val response: HttpResponse = client.get("http://${baseUrl.value}/satsystems")
            response.body()
        } catch (e: Exception) {
            Log.e("getSatelliteSystems", "Error fetching data: ${e.localizedMessage}", e)
//            "Error: ${e.localizedMessage}"
            return SatelliteSystems(0, 0, 0, 0)
        }
    }

    suspend fun getNtripStatus(): NtripStatus {
        return try {
            val response: HttpResponse = client.get("http://${baseUrl.value}/ntrip")
            response.body()
        } catch (e: Exception) {
            Log.e("getNtripStatus", "Error fetching data: ${e.localizedMessage}", e)
            return NtripStatus(false)
        }
    }

    suspend fun getPrecision(): String? {
        return try {
            val response: HttpResponse = client.get("http://${baseUrl.value}/precision")
            response.bodyAsText()
        } catch (e: Exception) {
            Log.e("getPrecision", "Error fetching data: ${e.localizedMessage}", e)
            "Error: ${e.localizedMessage}"
        }
    }

    suspend fun getPosition(): String? {
        return try {
            val response: HttpResponse = client.get("http://${baseUrl.value}/position")
            response.bodyAsText()
        } catch (e: Exception) {
            Log.e("getPosition", "Error fetching data: ${e.localizedMessage}", e)
            "Error: ${e.localizedMessage}"
        }
    }

    suspend fun setUpdateRate(updateRate: UpdateRate): String? {
        return try {
            val response: HttpResponse = client.post("http://${baseUrl.value}/rate") {
                contentType(ContentType.Application.Json)
                setBody(updateRate)
            }
            response.bodyAsText()
        } catch (e: Exception) {
            Log.e("setUpdateRate", "Error fetching data: ${e.localizedMessage}", e)
            "Error: ${e.localizedMessage}"
        }
    }

    suspend fun setSatelliteSystems(
        bds: Int,
        gps: Int,
        glo: Int,
        gal: Int
    ): String? {
        return try {
            val response: HttpResponse = client.post("http://${baseUrl.value}/satsystems") {
                contentType(ContentType.Application.Json)
                setBody(
                    SatelliteSystems(
                        bds = bds,
                        gps = gps,
                        glo = glo,
                        gal = gal
                    )
                )
            }
            response.bodyAsText()
        } catch (e: Exception) {
            Log.e("setSatelliteSystems", "Error fetching data: ${e.localizedMessage}", e)
            "Error: ${e.localizedMessage}"
        }
    }

    suspend fun setNtripStatus(ntripStatus: NtripStatus): String? {
        return try {
            val response: HttpResponse = client.post("http://${baseUrl.value}/ntrip") {
                contentType(ContentType.Application.Json)
                setBody(ntripStatus)
            }
            response.status.value.toString()
        } catch (e: Exception) {
            Log.e("setNtripStatus", "Error fetching data: ${e.localizedMessage}", e)
            "Error: ${e.localizedMessage}"
        }
    }

    suspend fun setPrecision(hAcc: String, vAcc: String): String? {
        return try {
            val response: HttpResponse = client.post("http://${baseUrl.value}/precision") {
                contentType(ContentType.Application.Json)
                setBody(
                    Precision(
                        hAcc = hAcc,
                        vAcc = vAcc
                    )
                )
            }
            response.bodyAsText()
        } catch (e: Exception) {
            Log.e("setPrecision", "Error fetching data: ${e.localizedMessage}", e)
            "Error: ${e.localizedMessage}"
        }
    }
}