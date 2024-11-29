package com.example.gnsstrackingapp.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class GnssClient {
    private val client = HttpClient()

    suspend fun getGnssData(): String {
        val response = client.get(
            urlString = "http://192.168.2.225/wstest.html",
        )

        return response.body()
    }
}