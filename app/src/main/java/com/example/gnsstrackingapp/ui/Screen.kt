package com.example.gnsstrackingapp.ui

open class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object MapScreen : Screen("map_screen")
    object StatisticsScreen : Screen("stat_screen")
    object SettingsScreen : Screen("settings_screen")
}