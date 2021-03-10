package com.kumela.runn.core.events

data class RunSessionInfoEvent(
    val fullDistanceInKm: Double,
    val speedInKmH: Double,
    val averageSpeed: Double,
    val fullDurationInMillis: Long
)