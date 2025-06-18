package de.hhn.externalgnsstrackingapp.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import de.hhn.externalgnsstrackingapp.R
import de.hhn.externalgnsstrackingapp.data.FixType
import de.hhn.externalgnsstrackingapp.data.GnssOutput
import de.hhn.externalgnsstrackingapp.ui.screens.settings.SettingsViewModel
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun StatisticsScreen(
    settingsViewModel: SettingsViewModel,
    statisticsViewModel: StatisticsViewModel,
) {
    val gnssOutput by statisticsViewModel.gnssOutput
    val provider = settingsViewModel.webServicesProvider.collectAsState().value
    val isConnected = provider?.connected?.collectAsState()?.value ?: false

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = stringResource(R.string.statistics),
            fontSize = Typography().headlineLarge.fontSize
        )

        ConnectedWebSocketChip(isConnected)

        ElevatedCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.time))
                    Text(text = if (gnssOutput.time.isNotEmpty()) parseTime(gnssOutput.time)
                    else "N/A")
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.latitude))
                    Text(
                        text = if (gnssOutput.lat.isNotEmpty()) "${
                            BigDecimal(convertToDecimalDegrees(gnssOutput.lat)).setScale(
                                7,
                                RoundingMode.HALF_UP
                            )
                        }°" else "N/A"
                    )
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.longitude))
                    Text(
                        text = if (gnssOutput.lon.isNotEmpty()) "${
                            BigDecimal(convertToDecimalDegrees(gnssOutput.lon)).setScale(
                                7,
                                RoundingMode.HALF_UP
                            )
                        }°" else "N/A"
                    )
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.fix_type))
                    Text(text = FixType.fromValue(gnssOutput.fixType).description)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.horizontal_accuracy))
                    Text(text = ("${gnssOutput.hAcc.toFloat() / 1000} m"))
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.vertical_accuracy))
                    Text(text = ("${gnssOutput.vAcc.toFloat() / 1000} m"))
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.elevation))
                    Text(text = if (gnssOutput.elev.isNotEmpty()) "${gnssOutput.elev} m" else "N/A")
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.rtcm_enabled))
                    Text(text = gnssOutput.rtcmEnabled.toString())
                }
            }
        }
    }
}

@Composable
fun ConnectedWebSocketChip(isConnected: Boolean) {
    val chipColors = if (isConnected) {
        AssistChipDefaults.assistChipColors(containerColor = Color.Green)
    } else {
        AssistChipDefaults.assistChipColors(containerColor = Color.Red)
    }

    AssistChip(
        onClick = {},
        label = {
            Text(
                text = if (isConnected) "Connected to Rover" else "Disconnected from Rover",
                color = Color.Black
            )
        },
        colors = chipColors
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

fun parseTime(time: String): String {
    return time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6)
}

private fun convertToDecimalDegrees(coordinate: String): Double {
    if (coordinate.isEmpty()) return 0.0
    val rawValue = coordinate.toDouble()
    val degrees = rawValue.toInt() / 100
    val minutes = rawValue % 100
    return degrees + (minutes / 60)
}
