package de.hhn.gnsstrackingapp.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import de.hhn.gnsstrackingapp.ui.Screen

@Composable
fun NavigationBarComponent(navController: NavController) {
    val selectedItem = remember { mutableIntStateOf(0) }
    val items = listOf("Map", "Statistics", "Settings")
    val screens =
        listOf(
            Screen.MapScreen,
            Screen.StatisticsScreen,
            Screen.SettingsScreen
        )
    val selectedIcons =
        listOf(
            Icons.Filled.LocationOn,
            Icons.Filled.Info,
            Icons.Filled.Settings
        )
    val unselectedIcons =
        listOf(
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
