package de.hhn.gnsstrackingapp.ui.screens.map

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.hhn.gnsstrackingapp.ui.screens.statistics.StatisticsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

data class LocationData(
    val location: GeoPoint = GeoPoint(49.122666, 9.209987),
    val locationName: String = "Heilbronn",
    val accuracy: Float = 0.0f
)

class LocationViewModel(statisticsViewModel: StatisticsViewModel) : ViewModel() {
    private val _locationData = MutableStateFlow(LocationData())
    val locationData: StateFlow<LocationData> = _locationData

    private val gnssOutput by statisticsViewModel.gnssOutput

    fun updateLocation(location: GeoPoint, locationName: String, accuracy: Float) {
        viewModelScope.launch {
            Log.d("gnss_sum", gnssOutput.toString())
            Log.d("gnss", "${gnssOutput.lat}, ${gnssOutput.lon}")
            if (gnssOutput.lat.isNotEmpty() && gnssOutput.lon.isNotEmpty()) {
                _locationData.emit(
                    LocationData(
                        GeoPoint(
                            GeoPoint(
                                convertToDecimalDegrees(gnssOutput.lat),
                                convertToDecimalDegrees(gnssOutput.lon)
                            )
                        ),
                        locationName,
                        0.5f * ((gnssOutput.hAcc.toFloat() + gnssOutput.vAcc.toFloat()) / 1000)
                    )
                )
            } else {
                _locationData.emit(LocationData(location, locationName, accuracy))
            }
        }
    }

    private fun convertToDecimalDegrees(coordinate: String): Double {
        if (coordinate.isEmpty()) return 0.0
        val rawValue = coordinate.toDouble()
        val degrees = rawValue.toInt() / 100
        val minutes = rawValue % 100
        return degrees + (minutes / 60)
    }
}
