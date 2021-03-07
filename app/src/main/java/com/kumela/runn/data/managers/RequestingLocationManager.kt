package com.kumela.runn.data.managers

import android.content.SharedPreferences
import javax.inject.Inject

class RequestingLocationManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun requestingLocationUpdates(): Boolean {
        return sharedPreferences.getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false)
    }

    fun setRequestingLocationUpdates(requesting: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requesting)
            .apply()
    }

    companion object {
        private const val KEY_REQUESTING_LOCATION_UPDATES = "KEY_REQUESTING_LOCATION_UPDATES"
    }
}