package com.example.gnsstrackingapp

import android.Manifest
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.example.gnsstrackingapp.ui.MainNavigation
import com.example.gnsstrackingapp.ui.Screen
import com.example.gnsstrackingapp.ui.theme.GNSSTrackingAppTheme
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(
            applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        getLocationPermissions()

        setContent {
            GNSSTrackingAppTheme {
                val navHostController = rememberNavController()
                val currentLocation = remember {
                    mutableStateOf(GeoPoint(48.947410, 9.144216))
                }

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

//    private fun lastLocation(): GeoPoint? {
//        val fusedLocationClient: FusedLocationProviderClient =
//            LocationServices.getFusedLocationProviderClient(this)
//        lateinit var lastLocation: GeoPoint
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return null
//        }
//        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//            lastLocation = if (location != null) {
//                GeoPoint(location.latitude, location.longitude)
//            } else {
//                GeoPoint(48.947410, 9.144216)
//            }
//        }
//
//        return lastLocation
//    }
}

@Composable
fun NavigationBarComponent(navController: NavController) {
    val selectedItem = remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Map", "Statistics", "Settings")
    val screens =
        listOf(Screen.HomeScreen, Screen.MapScreen, Screen.StatisticsScreen, Screen.SettingsScreen)
    val selectedIcons =
        listOf(Icons.Filled.Home, Icons.Filled.LocationOn, Icons.Filled.Info, Icons.Filled.Settings)
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

//@Composable
//fun CurrentLocationScreen() {
//    val permissions = listOf(
//        Manifest.permission.ACCESS_COARSE_LOCATION,
//        Manifest.permission.ACCESS_FINE_LOCATION,
//    )
//    PermissionBox(
//        permissions = permissions,
//        requiredPermissions = listOf(permissions.first()),
//        onGranted = {
//            CurrentLocationContent(
//                usePreciseLocation = it.contains(Manifest.permission.ACCESS_FINE_LOCATION),
//            )
//        },
//    )
//}


//@RequiresPermission(
//    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
//)
//@Composable
//fun CurrentLocationContent(usePreciseLocation: Boolean) {
//    val scope = rememberCoroutineScope()
//    val context = LocalContext.current
//    val locationClient = remember {
//        LocationServices.getFusedLocationProviderClient(context)
//    }
//    var locationInfo by remember {
//        mutableStateOf("")
//    }
//
//    Column(
//        Modifier
//            .fillMaxWidth()
//            .animateContentSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        Button(
//            onClick = {
//                // getting last known location is faster and minimizes battery usage
//                // This information may be out of date.
//                // Location may be null as previously no client has access location
//                // or location turned of in device setting.
//                // Please handle for null case as well as additional check can be added before using the method
//                scope.launch(Dispatchers.IO) {
//                    val result = async { locationClient.lastLocation }.await()
//                    locationInfo = if (result == null) {
//                        "No last known location. Try fetching the current location first"
//                    } else {
//                        "Current location is \n" + "lat : ${result.latitude}\n" +
//                                "long : ${result.longitude}\n" + "fetched at ${System.currentTimeMillis()}"
//                    }
//                }
//            },
//        ) {
//            Text("Get last known location")
//        }
//
//        Button(
//            onClick = {
//                //To get more accurate or fresher device location use this method
//                scope.launch(Dispatchers.IO) {
//                    val priority = if (usePreciseLocation) {
//                        Priority.PRIORITY_HIGH_ACCURACY
//                    } else {
//                        Priority.PRIORITY_BALANCED_POWER_ACCURACY
//                    }
//                    val result = locationClient.getCurrentLocation(
//                        priority,
//                        CancellationTokenSource().token,
//                    ).await()
//                    result?.let { fetchedLocation ->
//                        locationInfo =
//                            "Current location is \n" + "lat : ${fetchedLocation.latitude}\n" +
//                                    "long : ${fetchedLocation.longitude}\n" + "fetched at ${System.currentTimeMillis()}"
//                    }
//                }
//            },
//        ) {
//            Text(text = "Get current location")
//        }
//        Text(
//            text = locationInfo,
//        )
//    }
//}


