package de.hhn.gnsstrackingapp.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import de.hhn.gnsstrackingapp.R
import de.hhn.gnsstrackingapp.data.GnssOutput
import de.hhn.gnsstrackingapp.data.NtripStatus
import de.hhn.gnsstrackingapp.data.SatelliteSystems
import de.hhn.gnsstrackingapp.network.RestApiClient
import de.hhn.gnsstrackingapp.network.WebServicesProvider

@Composable
fun StatisticsScreen(
    statisticsViewModel: StatisticsViewModel,
    webServicesProvider: WebServicesProvider
) {
    val gnssOutput by statisticsViewModel.gnssOutput // Observe gnssOutput from ViewModel
    val gnssSatelliteSystemsState = remember { mutableStateOf(SatelliteSystems(0, 0, 0, 0)) }
    val gnssNtripStatusState = remember { mutableStateOf(NtripStatus(false)) }

    // Fetch GNSS data when the composable is displayed
    LaunchedEffect(Unit) {
        if (webServicesProvider.connected.value) {
            val client = RestApiClient()
            gnssSatelliteSystemsState.value = client.getSatelliteSystems()

            client.setNtripStatus(NtripStatus(true))
            gnssNtripStatusState.value = client.getNtripStatus()
        }
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = stringResource(R.string.statistics),
            fontSize = Typography().headlineLarge.fontSize
        )

        ConnectedWebSocketChip(webServicesProvider)

        ElevatedCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = stringResource(R.string.time, gnssOutput.time))
                Text(text = stringResource(R.string.longitude, gnssOutput.lon))
                Text(text = stringResource(R.string.latitude, gnssOutput.lat))
                Text(text = stringResource(R.string.fix_type, gnssOutput.fixType))
                Text(text = stringResource(R.string.horizontal_accuracy, gnssOutput.hAcc))
                Text(text = stringResource(R.string.vertical_accuracy, gnssOutput.vAcc))
                Text(text = stringResource(R.string.elevation, gnssOutput.elev))
                Text(text = stringResource(R.string.rtcm_enabled, gnssOutput.rtcmEnabled))
            }
        }

        ElevatedCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                gnssSatelliteSystemsState.value.let { data ->
                    Text(text = "{bds: ${data.bds}, gps: ${data.gps}, glo: ${data.glo}, gal: ${data.gal}}")
                }
            }
        }

//        ElevatedCard {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                gnssNtripStatusState.value?.let { data ->
//                    Text(text = data)
//                } ?: Text(text = "Loading...")
//            }
//        }
    }
}


@Composable
fun ConnectedWebSocketChip(webServicesProvider: WebServicesProvider) {
    val chipColors: ChipColors = if (webServicesProvider.connected.value) {
        AssistChipDefaults.assistChipColors(
            containerColor = Color.Green
        )
    } else {
        AssistChipDefaults.assistChipColors(
            containerColor = Color.Red
        )
    }

    AssistChip(onClick = {}, label = {
        Text(
            text = if (webServicesProvider.connected.value) stringResource(R.string.connected_to_websocket)
            else stringResource(R.string.disconnected_from_websocket)
        )
    }, colors = chipColors
    )
}


fun parseGnssJson(json: String): GnssOutput {
    return try {
        Gson().fromJson(json, GnssOutput::class.java)
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
            exception = null
        )
    }
}



