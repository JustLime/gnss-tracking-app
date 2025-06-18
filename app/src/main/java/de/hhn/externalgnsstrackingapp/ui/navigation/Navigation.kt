package de.hhn.externalgnsstrackingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.hhn.externalgnsstrackingapp.ui.screens.map.LocationViewModel
import de.hhn.externalgnsstrackingapp.ui.screens.map.MapScreen
import de.hhn.externalgnsstrackingapp.ui.screens.map.MapViewModel
import de.hhn.externalgnsstrackingapp.ui.screens.settings.SettingsScreen
import de.hhn.externalgnsstrackingapp.ui.screens.settings.SettingsViewModel
import de.hhn.externalgnsstrackingapp.ui.screens.statistics.StatisticsScreen
import de.hhn.externalgnsstrackingapp.ui.screens.statistics.StatisticsViewModel

@Composable
fun MainNavigation(
    navHostController: NavHostController = rememberNavController(),
    mapViewModel: MapViewModel,
    locationViewModel: LocationViewModel,
    statisticsViewModel: StatisticsViewModel,
    settingsViewModel: SettingsViewModel,
) {
    NavHost(navController = navHostController, startDestination = Screen.MapScreen.route) {
        composable(Screen.MapScreen.route) { MapScreen(mapViewModel, locationViewModel) }
        composable(Screen.StatisticsScreen.route) {
            StatisticsScreen(
                settingsViewModel, statisticsViewModel
            )
        }
        composable(Screen.SettingsScreen.route) { SettingsScreen(settingsViewModel) }
    }
}
