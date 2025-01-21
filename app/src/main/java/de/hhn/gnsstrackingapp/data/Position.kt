package de.hhn.gnsstrackingapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val time: String,
    val lat: String,
    val lon: String,
    val elev: String,
    val fixType: Int
)
