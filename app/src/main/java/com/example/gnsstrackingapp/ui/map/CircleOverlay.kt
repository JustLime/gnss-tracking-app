package com.example.gnsstrackingapp.ui.map

import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import com.example.gnsstrackingapp.ui.theme.Purple40
import com.example.gnsstrackingapp.ui.theme.Purple80
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay

/**
 * An overlay that draws a circle at a given [GeoPoint] with a size relative to the screen size.
 *
 * The size is specified as a fraction of the screen size, e.g. 0.05 for 5% of the screen size.
 *
 * @param center The center of the circle.
 * @param fractionOfScreen The fraction of the screen size of the circle.
 */
class CircleOverlay(
    private val center: GeoPoint,
    private val fractionOfScreen: Float, // Fraction of screen size (e.g., 0.05 for 5%)
) : Overlay() {
    private val fillColor: Int =
        Purple80.copy(alpha = 0.7f).toArgb()
    private val strokeColor: Int =
        Purple40.toArgb()
    private val strokeWidthCircle: Float = 5f // Default stroke width

    private val paint = Paint().apply {
        color = fillColor
        strokeWidth = strokeWidthCircle
        style = Paint.Style.FILL_AND_STROKE
        isAntiAlias = true
    }

    private val strokePaint = Paint().apply {
        color = strokeColor
        strokeWidth = strokeWidthCircle
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {
        super.draw(canvas, mapView, shadow)

        // Get screen resolution (width and height of the map view)
        val screenWidth = mapView.width

        // Calculate the radius of the circle as a fraction of the screen width (or height)
        val radiusInPixels =
            (screenWidth * fractionOfScreen).toInt() // Use screen width for the radius calculation

        // Convert GeoPoint (lat, lon) to screen coordinates
        val projection = mapView.projection
        val screenPoint = projection.toPixels(center, null)

        // Draw the circle fill
        canvas.drawCircle(
            screenPoint.x.toFloat(),
            screenPoint.y.toFloat(),
            radiusInPixels.toFloat(),
            paint // Fill color
        )

        // Draw the circle stroke (outline)
        canvas.drawCircle(
            screenPoint.x.toFloat(),
            screenPoint.y.toFloat(),
            radiusInPixels.toFloat(),
            strokePaint // Stroke color
        )
    }
}