package de.hhn.externalgnsstrackingapp.ui.screens.settings

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.hhn.externalgnsstrackingapp.data.NtripStatus
import de.hhn.externalgnsstrackingapp.data.UpdateRate
import de.hhn.externalgnsstrackingapp.network.RestApiClient
import de.hhn.externalgnsstrackingapp.ui.theme.Purple40
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    settingsViewModel.websocketIp.value

    val client = RestApiClient(settingsViewModel)
    val coroutineScope = rememberCoroutineScope()
    val textState = remember { mutableStateOf(TextFieldValue(text = "")) }
    val snackbarHostState = SnackbarHostState()

    val focusManager = LocalFocusManager.current

    // Fetch initial status and update rate
    LaunchedEffect(Unit) {
        try {
            Log.d("SettingsScreen", "Fetching initial NTRIP status...")
            settingsViewModel.ntripStatus.value = client.getNtripStatus()

            settingsViewModel.updateRate.value = client.getUpdateRate()
            textState.value =
                TextFieldValue(text = settingsViewModel.updateRate.value.updateRate.toString())

            settingsViewModel.satelliteSystems.value = client.getSatelliteSystems()
        } catch (e: Exception) {
            Log.e("SettingsScreen", "Error fetching data: ${e.localizedMessage}")
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }, verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        item {
            Text(
                text = "Settings", style = MaterialTheme.typography.headlineLarge
            )
            Text(text = "Rover URL: ${settingsViewModel.websocketIp.value}")
        }

        val websocketIpTextState =
            mutableStateOf(TextFieldValue(settingsViewModel.websocketIp.value))


        item {
            SettingsListItem(title = "Websocket IP",
                description = "Sets the IP address of the GNSS receiver.",
                icon = Icons.Outlined.Build,
                content = {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextField(
                            value = websocketIpTextState.value, onValueChange = {
                                websocketIpTextState.value = it
                            }, modifier = Modifier.requiredWidth(150.dp)
                        )
                        Button(
                            onClick = {
                                val inputIp = websocketIpTextState.value.text.trim()
                                coroutineScope.launch {
                                    focusManager.clearFocus()

                                    if (isValidIp(inputIp)) {
                                        settingsViewModel.restartWebSocket(inputIp)
                                        Log.d("SettingsScreen", "WebSocket restarted with new IP.")

                                        snackbarHostState.showSnackbar(
                                            message = "IP address updated successfully.",
                                            actionLabel = "OK"
                                        )
                                    } else {
                                        Log.e(
                                            "SettingsScreen",
                                            "Invalid IP address entered: $inputIp"
                                        )
                                        snackbarHostState.showSnackbar(
                                            message = "Invalid IP address. Please enter a valid IPv4 address.",
                                            actionLabel = "OK"
                                        )
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Purple40,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Update")
                        }
                    }
                })
        }

        item {
            SettingsListItem(title = "Toggle NTRIP",
                description = "Turns NTRIP data on/off.",
                icon = Icons.Outlined.Build,
                content = {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                client.setNtripStatus(NtripStatus(true))
                            }
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Purple40, contentColor = Color.White
                        )
                    ) {
                        Text(if (settingsViewModel.ntripStatus.value.enabled) "Turn off" else "Turn on")
                    }
                })
        }

        item {
            SettingsListItem(title = "Update rate",
                description = "Sets the GNSS receiver's update rate in milliseconds (Max: 5000).",
                icon = Icons.Outlined.Build,
                content = {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        TextField(
                            value = textState.value,
                            onValueChange = {
                                textState.value = it
                                settingsViewModel.updateRate.value.updateRate =
                                    it.text.toIntOrNull()?.coerceIn(0, 5000) ?: 0
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            maxLines = 1,
                            modifier = Modifier.requiredWidth(150.dp)
                        )
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    client.setUpdateRate(UpdateRate(settingsViewModel.updateRate.value.updateRate))
                                    Log.d(
                                        "SettingsScreen",
                                        "Update rate set to: ${settingsViewModel.updateRate.value.updateRate}"
                                    )
                                }
                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = Purple40, contentColor = Color.White
                            )
                        ) {
                            Text("Update")
                        }
                    }
                })
        }

        item {
            SettingsListItem(title = "Activated satellite systems",
                description = "Configure satellite systems to activate.",
                icon = Icons.Outlined.Build,
                content = { SatelliteSystemsSettings(settingsViewModel) })
        }
    }

    SnackbarHost(
        hostState = snackbarHostState
    )
}


@Composable
fun SettingsListItem(
    title: String,
    description: String,
    icon: ImageVector,
    contentDescription: String? = null,
    content: @Composable ColumnScope.() -> Unit = {},
    onClick: () -> Unit = {},
) {
    val typography = Typography()

    AssistChip(onClick = onClick, label = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(0.dp, 16.dp, 0.dp, 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Icon(imageVector = icon, contentDescription = contentDescription)
                Column {
                    Column(
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = typography.bodyLarge.fontSize,
                        )
                        Text(
                            text = description,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraLight,
                            lineHeight = 16.sp,
                            color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(content = content)
                }
            }
        }
    })
}

@Composable
fun SatelliteSystemsSwitch(checked: Boolean,onCheckedChange: (Boolean) -> Unit, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Purple40
            )
        )
        Text(label)
    }
}

@Composable
fun SatelliteSystemsSettings(viewModel: SettingsViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SatelliteSystemsSwitch(
                checked  = viewModel.gpsChecked.value,
                onCheckedChange = { viewModel.updateGps(it) },
                label = "GPS"
            )
            SatelliteSystemsSwitch(
                checked  = viewModel.bdsChecked.value,
                onCheckedChange = { viewModel.updateBds(it) },
                label = "BeiDou"
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SatelliteSystemsSwitch(
                checked  = viewModel.gloChecked.value,
                onCheckedChange = { viewModel.updateGlo(it) },
                label = "Glonass"
            )
            SatelliteSystemsSwitch(
                checked  = viewModel.galChecked.value,
                onCheckedChange = { viewModel.updateGal(it) },
                label = "Galileo"
            )
        }
    }
}

fun isValidIp(ip: String): Boolean {
    val ipRegex =
        Regex("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\$")

    return ipRegex.matches(ip)
}


