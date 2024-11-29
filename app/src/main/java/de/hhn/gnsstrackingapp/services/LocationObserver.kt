package de.hhn.gnsstrackingapp.services

interface LocationObserver {
    fun onLocationUpdated(
        locationName: String,
        latitude: Double,
        longitude: Double,
        accuracy: Float
    )
}