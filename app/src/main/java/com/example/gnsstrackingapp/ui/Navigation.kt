package com.example.gnsstrackingapp.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gnsstrackingapp.ui.home.HomeScreen
import com.example.gnsstrackingapp.ui.map.MapScreen
import com.example.gnsstrackingapp.ui.map.MapViewModel
import com.example.gnsstrackingapp.ui.settings.SettingsScreen
import com.example.gnsstrackingapp.ui.statistics.StatisticsScreen
import org.osmdroid.util.GeoPoint

@Composable
fun MainNavigation(
    navHostController: NavHostController = rememberNavController(),
    currentLocation: GeoPoint,
) {
    val mapViewModel = viewModel<MapViewModel>()

    NavHost(navController = navHostController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) { HomeScreen(navHostController, currentLocation) }
        composable(Screen.MapScreen.route) { MapScreen(mapViewModel) }
        composable(Screen.StatisticsScreen.route) { StatisticsScreen(navHostController) }
        composable(Screen.SettingsScreen.route) { SettingsScreen(navHostController) }
    }
}