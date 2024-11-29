package com.example.gnsstrackingapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.gnsstrackingapp.services.LocationService
import com.example.gnsstrackingapp.services.ServiceManager
import com.example.gnsstrackingapp.ui.MainNavigation
import com.example.gnsstrackingapp.ui.composables.NavigationBarComponent
import com.example.gnsstrackingapp.ui.theme.GNSSTrackingAppTheme
import com.example.gnsstrackingapp.ui.viewmodels.LocationViewModel
import org.osmdroid.mapsforge.MapsForgeTileProvider
import org.osmdroid.mapsforge.MapsForgeTileSource
import org.osmdroid.util.GeoPoint


class MainActivity : ComponentActivity() {
    private lateinit var serviceManager: ServiceManager
    private val locationViewModel: LocationViewModel by viewModels()
    var fromFiles: MapsForgeTileSource? = null
    var forge: MapsForgeTileProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        serviceManager = ServiceManager(this)

        MapsForgeTileSource.createInstance(this.application)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val allGranted = serviceManager.requiredPermissions.all { permissions[it] == true }
                if (allGranted) {
                    serviceManager.startLocationService()
                } else {
                    Log.e("MainActivity", "Permissions not granted: $permissions")
                }
            }

        if (!serviceManager.arePermissionsGranted()) {
            requestPermissionLauncher.launch(serviceManager.requiredPermissions)
        } else {
            serviceManager.startLocationService()
        }

        LocationService.onLocationUpdate = { latitude, longitude, locationName, accuracy ->
            locationViewModel.updateLocation(GeoPoint(latitude, longitude), locationName, accuracy)
        }

        setContent {
            GNSSTrackingAppTheme {
                val navHostController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(bottomBar = {
                        NavigationBarComponent(navController = navHostController)
                    }, content = { padding ->
                        Column(Modifier.padding(padding)) {
                            MainNavigation(
                                navHostController, locationViewModel
                            )
                        }
                    })
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceManager.stopLocationService()
    }
}

