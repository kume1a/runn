package com.kumela.runn.data.db.run

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "run_sessions")
data class RunSession(
    val timestamp: Long,
    val caloriesBurned: Float,
    val distanceInKm: Double,
    val durationInMillis: Long,
    val averageSpeed: Double,
    val speeds: List<Double>,
    val coveredLocationPoints: List<LatLng>,
    val mapSnapshot: Bitmap,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}