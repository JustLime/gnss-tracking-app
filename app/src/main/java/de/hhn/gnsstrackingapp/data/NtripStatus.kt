package de.hhn.gnsstrackingapp.data

import kotlinx.serialization.Serializable

@Serializable
data class NtripStatus(
    val enabled: Boolean,
)
