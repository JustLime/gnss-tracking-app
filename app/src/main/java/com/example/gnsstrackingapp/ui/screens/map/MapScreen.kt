package com.example.gnsstrackingapp.ui.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gnsstrackingapp.ui.viewmodels.LocationData
import com.example.gnsstrackingapp.ui.viewmodels.LocationViewModel
import com.example.gnsstrackingapp.ui.viewmodels.MapViewModel

@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
    locationViewModel: LocationViewModel,
) {
    val mapView = rememberMapViewWithLifecycle()
    val isBottomSheetVisible = remember { mutableStateOf(false) }
    val locationData by locationViewModel.locationData.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        OsmMapView(
            mapView = mapView,
            viewModel = mapViewModel,
            locationViewModel = locationViewModel,
            onCircleClick = { isBottomSheetVisible.value = true }
        )

        LocationCard(locationData = locationData)

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            GetOwnLocationButton(onClick = {
                mapViewModel.centerLocation = locationViewModel.locationData.value.location
                mapViewModel.zoomLevel = 20.0
                mapViewModel.mapOrientation = 20f

                mapView.controller.animateTo(
                    locationViewModel.locationData.value.location,
                    mapViewModel.zoomLevel,
                    3000L,
                    mapViewModel.mapOrientation
                )
            })
        }

        if (isBottomSheetVisible.value) {
            StatBottomSheet(
                isVisible = isBottomSheetVisible.value,
                onDismiss = { isBottomSheetVisible.value = false }
            )
        }
    }
}

@Composable
fun GetOwnLocationButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() }, modifier = Modifier.padding(16.dp)
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
                horizontalArrangement = Arrangement.Center, // Center elements within the Row
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
                    modifier = Modifier.padding(start = 8.dp) // Add some spacing between icon and text
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly, // Even spacing for latitude and longitude
                verticalAlignment = Alignment.CenterVertically,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = if (isVisible) SheetValue.PartiallyExpanded else SheetValue.Hidden
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    LaunchedEffect(isVisible) {
        if (isVisible) {
            bottomSheetState.expand()
        } else {
            bottomSheetState.hide()
        }
    }

    LaunchedEffect(bottomSheetState.currentValue) {
        if (bottomSheetState.currentValue == SheetValue.Hidden) {
            onDismiss()
        }
    }

    BottomSheetScaffold(
        modifier = Modifier.background(
            color = CardDefaults.cardColors().containerColor,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ),
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "ZED-F9P",
                        fontSize = 20.sp,
                        fontWeight = Bold
                    )
                    Text(
                        text = "Connected: Yes",
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "Signal Strength: Strong",
                        fontSize = 16.sp,
                    )
                }
            }
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 100.dp,
        sheetDragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .height(8.dp)
                    .padding(top = 4.dp)
                    .background(Color.White, shape = RoundedCornerShape(3.dp)),
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding))
    }
}

fun getLatitudeDirection(latitude: Double): String {
    return if (latitude >= 0) "N" else "S" // North if positive, South if negative
}

fun getLongitudeDirection(longitude: Double): String {
    return if (longitude >= 0) "E" else "W" // East if positive, West if negative
}
