package de.hhn.gnsstrackingapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import de.hhn.gnsstrackingapp.ui.Screen
import de.hhn.gnsstrackingapp.ui.viewmodels.LocationViewModel

@Composable
fun HomeScreen(navController: NavController, locationViewModel: LocationViewModel) {
    val locationData by locationViewModel.locationData.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .padding(all = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent,
            ),

            ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.scale(1.5f)

                    )
                    Text(
                        text = locationData.locationName,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "${locationData.location.latitude}° N",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "${locationData.location.longitude}° O",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clickable {
                    navController.navigate(Screen.MapScreen.route)
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    )
            )
            {

            }
        }
    }
}