package de.hhn.externalgnsstrackingapp.data

import kotlinx.serialization.Serializable

@Serializable
data class SatelliteSystems(
    var bds: Int,
    var gps: Int,
    var glo: Int,
    var gal: Int,
)
