package de.hhn.gnsstrackingapp.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import de.hhn.gnsstrackingapp.data.GnssOutput
import de.hhn.gnsstrackingapp.network.WebServicesProvider
import kotlinx.coroutines.launch

@Composable
fun StatisticsScreen(statisticsViewModel: StatisticsViewModel) {
    val typography = Typography()
    val webSocketScope = rememberCoroutineScope()
    val startSocketScope = rememberCoroutineScope()

    // Launch WebSocket listener and handle updates
    LaunchedEffect(true) {
        webSocketScope.launch {
            val webServicesProvider = WebServicesProvider("ws://192.168.2.225:80")
            startSocketScope.launch {
                webServicesProvider.startSocket() // Start WebSocket connection
            }

            // Listen for updates from the WebSocket channel
            for (socketUpdate in webServicesProvider.socketEventChannel) {
                socketUpdate.text?.let { jsonData ->
                    statisticsViewModel.gnssOutput.value =
                        parseGnssJson(jsonData)
                }
            }
        }
    }

    // UI to display GNSS data
    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Statistics",
            fontSize = typography.headlineLarge.fontSize
        )

        ElevatedCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Time: ${statisticsViewModel.gnssOutput.value.time}")
                Text(text = "Longitude: ${statisticsViewModel.gnssOutput.value.lon}")
                Text(text = "Latitude: ${statisticsViewModel.gnssOutput.value.lat}")
                Text(text = "Fix Type: ${statisticsViewModel.gnssOutput.value.fixType}")
                Text(text = "Horizontal Accuracy: ${statisticsViewModel.gnssOutput.value.hAcc}")
                Text(text = "Vertical Accuracy: ${statisticsViewModel.gnssOutput.value.vAcc}")
                Text(text = "Elevation: ${statisticsViewModel.gnssOutput.value.elev}")
                Text(text = "RTCM Enabled: ${statisticsViewModel.gnssOutput.value.rtcmEnabled}")
            }
        }
    }
}

fun parseGnssJson(json: String): GnssOutput {
    // Assuming you're using Gson to parse the GNSS response:
    val gson = Gson()
    return try {
        gson.fromJson(json, GnssOutput::class.java)
    } catch (e: Exception) {
        GnssOutput(
            time = "",
            lon = "",
            lat = "",
            fixType = 0,
            hAcc = 0,
            vAcc = 0,
            elev = "",
            rtcmEnabled = false,
        )
    }
}
