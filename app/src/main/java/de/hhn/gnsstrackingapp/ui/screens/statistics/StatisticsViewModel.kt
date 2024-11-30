package de.hhn.gnsstrackingapp.ui.screens.statistics

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.hhn.gnsstrackingapp.data.GnssOutput

class StatisticsViewModel : ViewModel() {
    var gnssOutput =
        mutableStateOf(
            GnssOutput(
                time = "161931.00",
                lon = "00731.4644883",
                lat = "5037.7607264",
                fixType = 1,
                hAcc = 19838,
                vAcc = 32370,
                elev = "257.601",
                rtcmEnabled = false
            )
        )
}
