package com.example.gnsstrackingapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gnsstrackingapp.ui.home.HomeScreen
import com.example.gnsstrackingapp.ui.map.MapScreen
import com.example.gnsstrackingapp.ui.statistics.StatisticsScreen

@Composable
fun MainNavigation(navHostController: NavHostController = rememberNavController()) {
    NavHost(navController = navHostController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) { HomeScreen(navHostController) }
        composable(Screen.MapScreen.route) { MapScreen(navHostController) }
        composable(Screen.StatisticsScreen.route) { StatisticsScreen(navHostController) }
    }
}