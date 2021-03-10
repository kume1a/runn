package com.kumela.runn.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import com.kumela.runn.core.events.LocationEvent
import org.greenrobot.eventbus.EventBus

class LocationBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val location: Location? = intent?.getParcelableExtra(LocationUpdateService.EXTRA_LOCATION)
        val distance: Double? = intent?.getDoubleExtra(LocationUpdateService.EXTRA_DISTANCE, 0.0)

        if (location != null && distance != null) {
//            EventBus.getDefault().post(LocationEvent(location, distance))
        }
    }
}