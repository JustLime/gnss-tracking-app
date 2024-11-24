package com.example.gnsstrackingapp.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.gnsstrackingapp.CHANNEL_ID
import com.example.gnsstrackingapp.R
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale

class LocationService : Service() {

    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(1000)
        .setMaxUpdateDelayMillis(1000)
        .build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val latitude = locationResult.lastLocation?.latitude
            val longitude = locationResult.lastLocation?.longitude

            latitude?.let { lat ->
                longitude?.let { lon ->
                    val locationName =
                        getLocationName(lat, lon) // Always attempt to resolve location name
                    startForegroundNotification(lat, lon, locationName)
                }
            }
        }

        override fun onLocationAvailability(locationAvailability: LocationAvailability) {
            // Handle location availability changes if needed
        }
    }

    private var lastKnownLocationName: String = "Unknown Location"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationUpdates()
        return START_STICKY
    }

    private fun startLocationUpdates() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("LocationService", "Location permissions are not granted.")
            return
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun startForegroundNotification(
        latitude: Double,
        longitude: Double,
        locationName: String
    ) {
        // Update the location name only if it's valid
        if (locationName != "Unknown Location") {
            lastKnownLocationName = locationName
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Tracking Location")
            .setContentText("$lastKnownLocationName ($latitude, $longitude)")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setOngoing(true) // Makes the notification non-dismissible
            .build()

        startForeground(1, notification)
    }

    private fun getLocationName(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            // TODO: Get alternative for Geocoder deprecated methods
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                addresses[0].locality ?: "Unknown Location" // City name or fallback
            } else {
                "Unknown Location"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Unknown Location"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}
