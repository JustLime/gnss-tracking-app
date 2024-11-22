package com.example.gnsstrackingapp.ui.composables

import android.graphics.Paint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.gnsstrackingapp.R
import com.example.gnsstrackingapp.ui.theme.Purple40
import com.example.gnsstrackingapp.ui.theme.Purple80
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon


@Composable
fun OsmMapView(
    modifier: Modifier = Modifier,
    currentLocation: GeoPoint
) {
    // Zustand für die MapView
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    // Benutzeroberfläche
    Column(modifier = modifier.fillMaxSize()) {
        // Karte anzeigen
        AndroidView(
            factory = {
                mapView.apply {
                    setUseDataConnection(true)
                    setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
                    setMultiTouchControls(true)
                    zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

                    val mapController = controller
                    val zoomFactor = 16.0
                    val radiusOwnLocation = 3.0

                    val circle =
                        drawOwnLocationCircle(currentLocation, radiusOwnLocation, zoomFactor)

                    mapController.setZoom(zoomFactor)
                    mapController.setCenter(currentLocation)
                    overlays.add(circle)

                    invalidate() // Refresh
                }
            },
            modifier = Modifier.weight(1f) // Karte nimmt den verfügbaren Platz ein
        )

        // Steuerungs-Button
        Button(
            onClick = {
                // Beispiel: Zoom erhöhen
                mapView.controller.zoomIn()
                // Oder zu einer anderen Position springen
                mapView.controller.setCenter(GeoPoint(48.947410, 9.144216))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Bewegen und Zoomen")
        }
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
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver = remember(mapView) {
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            else -> {}
        }
    }
}

fun drawOwnLocationCircle(
    geoPoint: GeoPoint, radius: Double = 3.0, zoomFactor: Double = 1.0
): Polygon {
    val pointList = mutableListOf<GeoPoint>()

    for (i in 0..360) {
        pointList.add(
            GeoPoint(
                geoPoint.latitude, geoPoint.longitude
            ).destinationPoint(radius, i.toDouble())
        )
    }

    val circle = Polygon()
    circle.outlinePaint.color = Purple40.toArgb()
    circle.outlinePaint.strokeWidth = 10.0f
    circle.outlinePaint.style = Paint.Style.STROKE
    circle.fillPaint.color = Purple80.toArgb()
    circle.actualPoints.apply {
        addAll(pointList)
    }

    return circle
}
