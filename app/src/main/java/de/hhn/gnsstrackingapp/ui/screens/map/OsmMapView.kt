package de.hhn.gnsstrackingapp.ui.screens.map

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
import de.hhn.gnsstrackingapp.R
import de.hhn.gnsstrackingapp.ui.viewmodels.LocationViewModel
import de.hhn.gnsstrackingapp.ui.viewmodels.MapViewModel
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay


@Composable
fun OsmMapView(
    modifier: Modifier = Modifier,
    mapView: MapView,
    viewModel: MapViewModel,
    locationViewModel: LocationViewModel,
    onCircleClick: () -> Unit
) {
    val locationData by locationViewModel.locationData.collectAsState()

    val mapListener = object : MapListener {
        override fun onScroll(event: ScrollEvent?): Boolean {
            event?.let {
                val currentMapOrientation = it.source.mapOrientation
                viewModel.mapOrientation = currentMapOrientation
            }

            return true
        }

        override fun onZoom(event: ZoomEvent?): Boolean {
            event?.let {
                val currentZoomLevel = it.source.zoomLevelDouble
                viewModel.zoomLevel = currentZoomLevel
            }

            return true
        }

    }

    AndroidView(
        factory = { mapView },
        modifier = modifier.fillMaxSize(),
        update = { mapViewUpdate ->
            mapViewUpdate.apply {
                if (overlays.isEmpty()) {
                    setUseDataConnection(true)
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

                    val compassOverlay = CompassOverlay(context, this)
                    compassOverlay.enableCompass()

                    val rotationGestureOverlay = RotationGestureOverlay(this)
                    rotationGestureOverlay.isEnabled = true

                    val scaleOverlay = ScaleBarOverlay(this)
                    scaleOverlay.setCentred(true)
                    scaleOverlay.setScaleBarOffset(300, 50)

//                    overlays.add(compassOverlay)
                    overlays.add(rotationGestureOverlay)
                    overlays.add(scaleOverlay)
                }

                if (!hasMapListener(mapListener)) {
                    addMapListener(mapListener)
                }

                mapOrientation = viewModel.mapOrientation
                controller.setZoom(viewModel.zoomLevel)

                val existingCircle = mapView.overlays.find { it is CircleOverlay }
                existingCircle?.let {
                    mapView.overlays.remove(it)
                }

                val accuracyInMeters = locationData.accuracy
                val circleOverlay = CircleOverlay(
                    locationData.location,
                    0.03f,
                    accuracyInMeters,
                    onCircleClick,
                )
                mapView.overlays.add(circleOverlay)

                if (viewModel.centerLocation != locationData.location) {
                    viewModel.centerLocation = locationData.location

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

private fun MapView.hasMapListener(listener: MapListener): Boolean {
    return overlays.any { it == listener }
}