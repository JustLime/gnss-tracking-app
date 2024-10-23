package com.example.gnsstrackingapp.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.gnsstrackingapp.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    onLoad: ((map: MapView) -> Unit)? = null
) {
    val mapViewState = rememberMapViewWithLifecycle()

    AndroidView(
        { mapViewState },
        modifier
    ) {

            mapView ->
        onLoad?.invoke(mapView)

        mapView.setUseDataConnection(true)
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)

        val mapController = mapView.controller
        mapController.setZoom(20.0)

//        val locationNewOverlay = MyLocationNewOverlay(
//            mapView
//        )
//        locationNewOverlay.enableMyLocation()
//        mapView.overlays.add(locationNewOverlay)

//        val scaleBarOverlay = ScaleBarOverlay(mapView)
//        scaleBarOverlay.setCentred(true)
//        mapView.overlays.add(scaleBarOverlay)

        val startPoint = GeoPoint(48.947410, 9.144216)
        mapController.setCenter(startPoint)

        val marker = Marker(mapView)
        marker.position = startPoint
        marker.title = "Bietigheim-Bissingen"
        mapView.overlays.add(marker)

        // Handle marker click events
//        marker.setOnMarkerClickListener { _, _ ->
//            Toast.makeText(mapView.context, "Marker clicked!", Toast.LENGTH_SHORT).show()
//            true
//        }

        // Refresh the map
        mapView.invalidate()
    }
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
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
    }