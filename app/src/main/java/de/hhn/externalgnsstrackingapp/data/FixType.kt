package de.hhn.externalgnsstrackingapp.data

enum class FixType(val description: String) {
    NO_FIX("No fix"),
    GPS("GPS"),
    DGNSS("DGNSS"),
    PPS("PPS"),
    RTK_FIXED("RTK Fixed"),
    RTK_FLOAT("RTK Float"),
    ESTIMATED("Estimated (dead reckoning)"),
    MANUAL("Manual input mode"),
    SIMULATION("Simulation mode"),
    UNKNOWN("unknown");

    companion object {
        fun fromValue(value: Int): FixType {
            return entries.find { it.ordinal == value } ?: UNKNOWN
        }
    }
}