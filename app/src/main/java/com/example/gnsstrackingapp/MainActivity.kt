package com.example.gnsstrackingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.example.gnsstrackingapp.ui.MainNavigation
import com.example.gnsstrackingapp.ui.Screen
import com.example.gnsstrackingapp.ui.composables.rememberMapViewWithLifecycle
import com.example.gnsstrackingapp.ui.theme.GNSSTrackingAppTheme
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge(
//            statusBarStyle = SystemBarStyle.dark(
//                android.graphics.Color.RED,
//            )
//        )

        Configuration.getInstance().load(
            applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        setContent {
            GNSSTrackingAppTheme {
                val navHostController = rememberNavController()
                val mapView = rememberMapViewWithLifecycle()

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
                                MainNavigation(navHostController)
                            }
                        }
                    )
                }
            }
        }
    }
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
                    Icon(
                        if (selectedItem.intValue == index) selectedIcons[index]
                        else unselectedIcons[index],
                        contentDescription = item
                    )
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


