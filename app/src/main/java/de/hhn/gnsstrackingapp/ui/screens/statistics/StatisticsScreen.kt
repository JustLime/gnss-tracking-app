package de.hhn.gnsstrackingapp.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.hhn.gnsstrackingapp.data.GnssOutput
import de.hhn.gnsstrackingapp.network.GnssClient
import kotlinx.coroutines.launch

@Composable
fun StatisticsScreen() {
    val typography = Typography()
    val gnssOutput = remember {
        mutableStateOf(
            GnssOutput(
                time = "161931.00",
                lon = "00731.4644883",
                exception = "",
                lat = "5037.7607264",
                fixType = 1,
                hAcc = 19838,
                vAcc = 32370,
                elev = "257.601",
                rtcmEnabled = false
            )
        )
    }

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
                Text(text = "Time: ${gnssOutput.value.time}")
                Text(text = "Longitude: ${gnssOutput.value.lon}")
                Text(text = "Latitude: ${gnssOutput.value.lat}")
                Text(text = "Fix Type: ${gnssOutput.value.fixType}")
                Text(text = "Horizontal Accuracy: ${gnssOutput.value.hAcc}")
                Text(text = "Vertical Accuracy: ${gnssOutput.value.vAcc}")
                Text(text = "Elevation: ${gnssOutput.value.elev}")
                Text(text = "RTCM Enabled: ${gnssOutput.value.rtcmEnabled}")
            }
        }
    }
}

@Composable
fun GnssTestView() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scope = rememberCoroutineScope()
        var text by remember { mutableStateOf("Loading") }

        LaunchedEffect(true) {
            scope.launch {
                text = try {
                    GnssClient().getGnssData()
                } catch (e: Exception) {
                    e.localizedMessage ?: "error"
                }
            }
        }
    }
}
