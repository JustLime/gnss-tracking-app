package com.example.gnsstrackingapp.services

interface LocationObserver {
    fun onLocationUpdate(locationName: String, latitude: Double, longitude: Double)
}