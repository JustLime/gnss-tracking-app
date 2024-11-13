package com.example.gnsstrackingapp.ui.composables

import android.graphics.Paint
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
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon


@Composable
fun OsmMapView(
    modifier: Modifier = Modifier, onLoad: ((map: MapView) -> Unit)? = null
) {
    val mapViewState = rememberMapViewWithLifecycle()

    AndroidView(
        { mapViewState }, modifier
    ) {

            mapView ->
        onLoad?.invoke(mapView)

        mapView.setUseDataConnection(true)
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)

        val mapController = mapView.controller
        val zoomFactor = 20.0
        val radiusOwnLocation = 3.0
        val startPoint = GeoPoint(48.947410, 9.144216)
        val circle = drawOwnLocationCircle(startPoint, radiusOwnLocation, zoomFactor)

        mapController.setZoom(zoomFactor)
        mapController.setCenter(startPoint)
        
        mapView.overlays.add(circle)

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
    geoPoint: GeoPoint,
    radius: Double = 3.0,
    zoomFactor: Double = 1.0
): Polygon {
    val pointList = mutableListOf<GeoPoint>()

    for (i in 0..360) {
        pointList.add(
            GeoPoint(
                geoPoint.latitude,
                geoPoint.longitude
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
