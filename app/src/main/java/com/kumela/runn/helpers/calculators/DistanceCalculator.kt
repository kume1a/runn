package com.kumela.runn.helpers.calculators

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

@Suppress("unused", "MemberVisibilityCanBePrivate")
class DistanceCalculator @Inject constructor() {

    fun calculateDistance(latitude1: Double, longitude1: Double, latitude2: Double, longitude2: Double): Double {
        val theta = longitude1 - longitude2
        var distance = sin(degToRad(latitude1)) *
                sin(degToRad(latitude2)) +
                cos(degToRad(latitude1)) *
                cos(degToRad(latitude2)) *
                cos(degToRad(theta))
        distance = acos(distance)
        distance = radToDeg(distance)
        distance *= 60 * 1.1515
        return distance
    }

    fun calculateDistance(a: LatLng, b: LatLng): Double = calculateDistance(a.latitude, a.longitude, b.latitude, b.longitude)

    fun calculateDistance(a: Location, b: Location): Double = calculateDistance(a.latitude, a.longitude, b.latitude, b.longitude)

    private fun degToRad(deg: Double): Double = deg * Math.PI / 180.0

    private fun radToDeg(rad: Double): Double = rad * 180.0 / Math.PI
}