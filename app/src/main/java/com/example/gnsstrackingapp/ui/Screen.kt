package com.example.gnsstrackingapp.ui

sealed class Screen(
    val route: String,
    var hasUpdated: Boolean? = null
) {
    data object HomeScreen : Screen("home_screen")
    data object MapScreen : Screen("map_screen", false)
    data object StatisticsScreen : Screen("stat_screen")
    data object SettingsScreen : Screen("settings_screen")
}