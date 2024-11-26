package com.example.gnsstrackingapp.ui.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.gnsstrackingapp.R
import com.example.gnsstrackingapp.ui.map.CircleOverlay
import com.example.gnsstrackingapp.ui.map.MapViewModel
import com.example.gnsstrackingapp.ui.viewmodels.LocationViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay


@Composable
fun OsmMapView(
    modifier: Modifier = Modifier,
    mapView: MapView,
    viewModel: MapViewModel,
    locationViewModel: LocationViewModel
) {
    val locationData by locationViewModel.locationData.collectAsState()

    AndroidView(
        factory = { mapView },
        modifier = modifier.fillMaxSize(),
        update = { mapViewUpdate ->
            mapViewUpdate.apply {
                if (overlays.isEmpty()) {
                    setUseDataConnection(true)
                    setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
                    setMultiTouchControls(true)
                    zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

                    val compassOverlay = CompassOverlay(context, this)
                    compassOverlay.enableCompass()

                    val rotationGestureOverlay = RotationGestureOverlay(this)
                    rotationGestureOverlay.isEnabled = true

                    overlays.add(compassOverlay)
                    overlays.add(rotationGestureOverlay)
                }

                mapOrientation = viewModel.mapOrientation

                val existingCircle = mapView.overlays.find { it is CircleOverlay }
                existingCircle?.let {
                    mapView.overlays.remove(it)
                }

                val circleOverlay = CircleOverlay(locationData.location, 0.02f)
                mapView.overlays.add(circleOverlay)

                if (viewModel.centerLocation != locationData.location) {
                    viewModel.centerLocation = locationData.location
                    viewModel.zoomLevel = zoomLevelDouble

                    controller.animateTo(
                        viewModel.centerLocation,
                        viewModel.zoomLevel,
                        500
                    )
                }

                invalidate()
            }
        }
    )
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver = remember(mapView) {
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            else -> {}
        }
    }
}
