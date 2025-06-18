package de.hhn.externalgnsstrackingapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Precision(
    val hAcc: String,
    val vAcc: String
)
