package com.kumela.runn.core.events

import com.google.android.gms.maps.model.LatLng

data class LocationEvent(
    val lastLocation: LatLng,
    val currentLocation: LatLng,
    val bearing: Float
)