package de.hhn.externalgnsstrackingapp.data

import kotlinx.serialization.Serializable

@Serializable
data class UpdateRate(
    var updateRate: Int,
)
