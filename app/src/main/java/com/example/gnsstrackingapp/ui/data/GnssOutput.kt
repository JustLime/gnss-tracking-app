package com.example.gnsstrackingapp.ui.data

// {
// "time": "161931.00",
// "lon": "00731.4644883",
// "exception": null,
// "lat": "5037.7607264",
// "fixType": 1,
// "hAcc": 19838,
// "vAcc": 32370,
// "elev": "257.601",
// "rtcmEnabled": false
// }

data class GnssOutput(
    val time: String,
    val lon: String,
    val lat: String,
    val fixType: Int,
    val hAcc: Int,
    val vAcc: Int,
    val elev: String,
    val rtcmEnabled: Boolean,
    val exception: String
)
