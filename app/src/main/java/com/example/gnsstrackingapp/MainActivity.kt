package com.example.gnsstrackingapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.example.gnsstrackingapp.ui.MainNavigation
import com.example.gnsstrackingapp.ui.Screen
import com.example.gnsstrackingapp.ui.theme.GNSSTrackingAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint


class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation by mutableStateOf(GeoPoint(48.947410, 9.144216))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(
            applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocationPermissions()

        setContent {
            GNSSTrackingAppTheme {
                val navHostController = rememberNavController()
                val currentLocation = remember {
                    mutableStateOf(GeoPoint(48.947410, 9.144216))
                }
                currentLocation.value = currentLocationGps()
                // TODO: Add currentPlaceName dynamically
                val currentPlaceName = "Bietigheim-Bissingen"

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            NavigationBarComponent(navController = navHostController)
                        },
                        content = { padding ->
                            Column(Modifier.padding(padding)) {
                                MainNavigation(
                                    navHostController,
                                    currentLocation.value,
                                    currentPlaceName
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    private fun getLocationPermissions() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    false
                ) -> {
                    // Precise location access granted.
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                }

                else -> {
                    // No location access granted.
                }
            }
        }

        // ...

        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun currentLocationGps(): GeoPoint {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                val geoPoint = GeoPoint(location.latitude, location.longitude)
                currentLocation = geoPoint
            }
        }

        return currentLocation
    }

    @Composable
    fun NavigationBarComponent(navController: NavController) {
        val selectedItem = remember { mutableIntStateOf(0) }
        val items = listOf("Home", "Map", "Statistics", "Settings")
        val screens =
            listOf(
                Screen.HomeScreen,
                Screen.MapScreen,
                Screen.StatisticsScreen,
                Screen.SettingsScreen
            )
        val selectedIcons =
            listOf(
                Icons.Filled.Home,
                Icons.Filled.LocationOn,
                Icons.Filled.Info,
                Icons.Filled.Settings
            )
        val unselectedIcons =
            listOf(
                Icons.Outlined.Home,
                Icons.Outlined.LocationOn,
                Icons.Outlined.Info,
                Icons.Outlined.Settings
            )

        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        BadgedBox(badge = {
                            if (screens[index].hasUpdated == true) {
                                Badge(containerColor = Color.Green)
                            } else if (screens[index].hasUpdated == false) {
                                Badge(containerColor = Color.Red)
                            }
                        }) {
                            Icon(
                                if (selectedItem.intValue == index) selectedIcons[index]
                                else unselectedIcons[index],
                                contentDescription = item
                            )
                        }
                    },
                    label = { Text(item) },
                    selected = selectedItem.intValue == index,
                    onClick = {
                        selectedItem.intValue = index
                        navController.navigate(screens[index].route)
                    }
                )
            }
        }
    }
}
