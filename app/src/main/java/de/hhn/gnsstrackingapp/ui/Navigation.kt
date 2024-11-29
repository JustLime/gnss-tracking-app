package de.hhn.gnsstrackingapp.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.hhn.gnsstrackingapp.ui.screens.map.MapScreen
import de.hhn.gnsstrackingapp.ui.screens.settings.SettingsScreen
import de.hhn.gnsstrackingapp.ui.screens.statistics.StatisticsScreen
import de.hhn.gnsstrackingapp.ui.viewmodels.LocationViewModel
import de.hhn.gnsstrackingapp.ui.viewmodels.MapViewModel

@Composable
fun MainNavigation(
    navHostController: NavHostController = rememberNavController(),
    locationViewModel: LocationViewModel,
) {
    val mapViewModel = viewModel<MapViewModel>()

    NavHost(navController = navHostController, startDestination = Screen.MapScreen.route) {
        composable(Screen.MapScreen.route) { MapScreen(mapViewModel, locationViewModel) }
        composable(Screen.StatisticsScreen.route) { StatisticsScreen() }
        composable(Screen.SettingsScreen.route) { SettingsScreen(navHostController) }
    }
}