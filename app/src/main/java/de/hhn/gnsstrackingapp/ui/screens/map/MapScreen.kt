package de.hhn.gnsstrackingapp.ui.screens.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.hhn.gnsstrackingapp.ui.theme.Purple40

@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
    locationViewModel: LocationViewModel,
) {
    val mapView = rememberMapViewWithLifecycle()
    val locationData by locationViewModel.locationData.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        OsmMapView(
            mapView = mapView,
            mapViewModel = mapViewModel,
            locationViewModel = locationViewModel,
        )

        LocationCard(locationData = locationData)

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            GetOwnLocationButton(onClick = {
                if (!mapViewModel.isAnimating) {
                    mapViewModel.centerLocation = locationViewModel.locationData.value.location
                    mapViewModel.zoomLevel = 20.0

                    mapViewModel.isAnimating = true

                    mapView.controller.animateTo(
                        locationViewModel.locationData.value.location,
                        mapViewModel.zoomLevel,
                        3000L,
                        0f
                    )

                    mapViewModel.isAnimating = false
                }
            })
        }
    }
}

@Composable
fun GetOwnLocationButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() }, modifier = Modifier.padding(16.dp),
        containerColor = Purple40,
        contentColor = Color.White
    ) {
        Icon(Icons.Filled.LocationOn, "Floating action button.")
    }
}

@Composable
fun LocationCard(locationData: LocationData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.scale(1.2f)
                )
                Text(
                    text = locationData.locationName,
                    fontSize = 22.sp,
                    fontWeight = Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${locationData.location.latitude}° ${
                        getLatitudeDirection(
                            locationData.location.latitude
                        )
                    }",
                    fontSize = 16.sp
                )
                Text(
                    text = "${locationData.location.longitude}° ${
                        getLongitudeDirection(
                            locationData.location.longitude
                        )
                    }",
                    fontSize = 16.sp
                )
            }
        }
    }
}

fun getLatitudeDirection(latitude: Double): String {
    return if (latitude >= 0) "N" else "S" // North if positive, South if negative
}

fun getLongitudeDirection(longitude: Double): String {
    return if (longitude >= 0) "E" else "W" // East if positive, West if negative
}
