package com.kumela.runn.helpers.calculators

import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class SpeedCalculator @Inject constructor(private val distanceCalculator: DistanceCalculator) {

    /**
     * calculate speed between point [a] and point [b] in kilometers per hour
     */
    fun calculateSpeed(a: LatLng, b: LatLng, timeInMillis: Long): Double =
        distanceCalculator.calculateDistance(a, b) / millisToHours(timeInMillis)

    private fun millisToHours(timeInMillis: Long): Double = timeInMillis * 0.000000278
}