package com.example.gnsstrackingapp.ui.composables

import android.graphics.Paint
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.gnsstrackingapp.ui.map.MapViewModel
import com.example.gnsstrackingapp.ui.theme.Purple40
import com.example.gnsstrackingapp.ui.theme.Purple80
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay


@Composable
fun OsmMapView(
    modifier: Modifier = Modifier,
    mapView: MapView,
    viewModel: MapViewModel,
    currentLocation: GeoPoint
) {
    AndroidView(
        factory = { mapView },
        modifier = modifier.fillMaxSize(),
        update = { mapViewUpdate ->
            mapViewUpdate.apply {
                setUseDataConnection(true)
                setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
                setMultiTouchControls(true)
                zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

                viewModel.centerLocation = currentLocation

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
