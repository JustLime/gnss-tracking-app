package com.example.gnsstrackingapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.example.gnsstrackingapp.services.LocationService
import com.example.gnsstrackingapp.ui.MainNavigation
import com.example.gnsstrackingapp.ui.Screen
import com.example.gnsstrackingapp.ui.theme.GNSSTrackingAppTheme
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint

class MainActivity : ComponentActivity() {

    private val requiredPermissions = arrayOf(
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) POST_NOTIFICATIONS else null
    ).filterNotNull().toTypedArray()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = requiredPermissions.all { permissions[it] == true }
            if (allGranted) {
                startLocationService()
            } else {
                Log.e("MainActivity", "Permissions not granted: $permissions")
            }
        }

    private lateinit var locationReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(
            applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        checkAndRequestPermissions()

        setContent {
            GNSSTrackingAppTheme {
                val navHostController = rememberNavController()
                val currentPlaceName = "Bietigheim-Bissingen"
                val currentLocation = GeoPoint(48.947410, 9.144216)

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
                                    currentLocation,
                                    currentPlaceName
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val missingPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isEmpty()) {
            startLocationService()
        } else {
            requestPermissionLauncher.launch(missingPermissions.toTypedArray())
        }
    }

    private fun startLocationService() {
        val intent = Intent(this, LocationService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }
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
