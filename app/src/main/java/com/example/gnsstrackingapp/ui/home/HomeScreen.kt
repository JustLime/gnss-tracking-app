package com.example.gnsstrackingapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gnsstrackingapp.ui.Screen
import com.example.gnsstrackingapp.ui.composables.OsmMapView

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .padding(all = 16.dp)
//            .border(1.dp, Color.Black)
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),

            ) {

        }

        ElevatedCard(
            modifier = Modifier
                .height(300.dp)
                .clickable(onClick = { navController.navigate(Screen.MapScreen.route) })
        ) {
            OsmMapView()
        }
    }
}