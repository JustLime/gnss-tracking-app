package com.example.gnsstrackingapp.ui.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gnsstrackingapp.ui.composables.OsmMapView
import com.example.gnsstrackingapp.ui.composables.rememberMapViewWithLifecycle
import org.osmdroid.util.GeoPoint

@Composable
fun MapScreen(
    viewModel: MapViewModel,
    currentLocation: GeoPoint
) {
    val mapView = rememberMapViewWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            OsmMapView(mapView = mapView, viewModel = viewModel, currentLocation = currentLocation)

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxSize()
            ) {
                GetOwnLocationButton(onClick = {
                    viewModel.centerLocation = currentLocation
                    viewModel.zoomLevel = 20.0
                    viewModel.mapOrientation = 20f

                    mapView.controller.animateTo(
                        currentLocation,
                        viewModel.zoomLevel,
                        3000L,
                        viewModel.mapOrientation
                    )
                })
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatBottomSheet() {
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    BottomSheetScaffold(
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row {
                        Text(
                            text = "ZED-F9P",
                            fontSize = 20.sp,
                            fontWeight = Bold
                        )
                    }

                    Row {
                        Text(
                            text = "Connected: ",
                            fontSize = 16.sp,
                        )
                        Text(text = "yes")
                    }
                }
            }
        },
        scaffoldState = scaffoldState
    )
    {

    }
}

