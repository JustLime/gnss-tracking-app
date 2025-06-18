package de.hhn.gnsstrackingapp.ui.screens.settings

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.hhn.gnsstrackingapp.data.NtripStatus
import de.hhn.gnsstrackingapp.data.SatelliteSystems
import de.hhn.gnsstrackingapp.data.UpdateRate
import de.hhn.gnsstrackingapp.network.WebServicesProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    val websocketIp = mutableStateOf("192.168.19.60")
    private val _webServicesProvider = MutableStateFlow<WebServicesProvider?>(null)
    val webServicesProvider: StateFlow<WebServicesProvider?> = _webServicesProvider

    var ntripStatus = mutableStateOf(NtripStatus(enabled = false))
    val updateRate = mutableStateOf(UpdateRate(0))
    val satelliteSystems = mutableStateOf(SatelliteSystems(gps = 0, bds = 0, glo = 0, gal = 0))

    val gpsChecked = derivedStateOf { satelliteSystems.value.gps == 1 }
    val bdsChecked = derivedStateOf { satelliteSystems.value.bds == 1 }
    val gloChecked = derivedStateOf { satelliteSystems.value.glo == 1 }
    val galChecked = derivedStateOf { satelliteSystems.value.gal == 1 }

    fun updateGps(enabled: Boolean) {
        satelliteSystems.value = satelliteSystems.value.copy(gps = if (enabled) 1 else 0)
    }

    fun updateBds(enabled: Boolean) {
        satelliteSystems.value = satelliteSystems.value.copy(bds = if (enabled) 1 else 0)
    }

    fun updateGlo(enabled: Boolean) {
        satelliteSystems.value = satelliteSystems.value.copy(glo = if (enabled) 1 else 0)
    }

    fun updateGal(enabled: Boolean) {
        satelliteSystems.value = satelliteSystems.value.copy(gal = if (enabled) 1 else 0)
    }

    init {
        createAndStartWebSocket()
    }

    private fun createAndStartWebSocket() {
        val provider = WebServicesProvider("ws://${websocketIp.value}:80")
        _webServicesProvider.value = provider
        viewModelScope.launch {
            provider.startSocket()
        }
    }

    fun restartWebSocket(newIp: String) {
        websocketIp.value = newIp
        stopWebSocket()
        viewModelScope.launch {
            delay(500)
            createAndStartWebSocket()
        }
    }

    fun stopWebSocket() {
        _webServicesProvider.value?.stopSocket()
        _webServicesProvider.value = null
    }

    override fun onCleared() {
        super.onCleared()
        stopWebSocket()
    }
}

