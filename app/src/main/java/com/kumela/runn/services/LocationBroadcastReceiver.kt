package com.kumela.runn.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import timber.log.Timber

class LocationBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val location: Location? = intent?.getParcelableExtra(LocationUpdateService.EXTRA_LOCATION)
        Timber.d("location = $location")
    }
}