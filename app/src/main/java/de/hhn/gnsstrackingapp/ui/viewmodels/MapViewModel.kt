package de.hhn.gnsstrackingapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint

class MapViewModel : ViewModel() {
    var zoomLevel: Double = 16.0
    var centerLocation: GeoPoint = GeoPoint(48.947410, 9.144216)
    var mapOrientation: Float = 0f
}
