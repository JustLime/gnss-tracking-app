package com.example.gnsstrackingapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

data class LocationData(
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val locationName: String = "Unknown Location"
)

class LocationViewModel : ViewModel() {

    private val _locationData = MutableStateFlow(LocationData())
    val locationData: StateFlow<LocationData> = _locationData

    fun updateLocation(location: GeoPoint, locationName: String) {
        viewModelScope.launch {
            _locationData.emit(LocationData(location, locationName))
        }
    }
}
