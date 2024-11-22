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
import androidx.compose.ui.viewinterop.AndroidView
import com.example.gnsstrackingapp.ui.composables.drawOwnLocationCircle
import com.example.gnsstrackingapp.ui.composables.rememberMapViewWithLifecycle
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay

@Composable
fun MapScreen(
    viewModel: MapViewModel
) {
    val mapView = rememberMapViewWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize(),
                update = { mapView ->
                    mapView.apply {
                        setUseDataConnection(true)
                        setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
                        setMultiTouchControls(true)
                        zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

                        controller.setZoom(viewModel.zoomLevel)
                        controller.setCenter(viewModel.centerLocation)

                        mapView.mapOrientation = viewModel.mapOrientation

                        val compassOverlay = CompassOverlay(
                            this.context, mapView
                        )
                        compassOverlay.enableCompass()

                        val rotationGestureOverlay = RotationGestureOverlay(mapView)
                        rotationGestureOverlay.isEnabled = true

                        val circle = drawOwnLocationCircle(
                            viewModel.centerLocation, 3.0, viewModel.zoomLevel
                        )

                        overlays.clear()
                        overlays.add(circle)
                        overlays.add(compassOverlay)

                        invalidate()
                    }
                }
            )

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxSize()
            ) {
                GetOwnLocationButton(onClick = {
                    val newLocation = GeoPoint(48.947410, 9.144216)
                    viewModel.centerLocation = newLocation
                    viewModel.zoomLevel = 20.0
                    viewModel.mapOrientation = 20f

                    mapView.controller.animateTo(
                        newLocation,
                        viewModel.zoomLevel,
                        2000L,
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

