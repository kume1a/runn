package com.kumela.runn.ui.run

import android.app.Activity
import android.content.*
import android.os.IBinder
import com.google.android.gms.maps.model.LatLng
import com.kumela.runn.core.lifecycle.ScreenLifecycleTask
import com.kumela.runn.di.annotations.ScreenScope
import com.kumela.runn.services.LocationUpdateService
import javax.inject.Inject

@ScreenScope
class RunLocationServiceController @Inject constructor() : ScreenLifecycleTask() {

    private var bound = false
    private var service: LocationUpdateService? = null

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: LocationUpdateService.LocalBinder = service as LocationUpdateService.LocalBinder
            this@RunLocationServiceController.service = binder.getService()
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            this@RunLocationServiceController.service = null
            bound = false
        }
    }

    override fun onContextAvailable(context: Context, activity: Activity?) {
        super.onContextAvailable(context, activity)

        activity?.bindService(Intent(activity, LocationUpdateService::class.java), mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onActivityStarted(activity: Activity) {
        super.onActivityStarted(activity)
        activity.bindService(Intent(activity, LocationUpdateService::class.java), mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onActivityStopped(activity: Activity) {
        if (bound) {
            activity.unbindService(mServiceConnection)
            bound = false
        }
        super.onActivityStopped(activity)
    }

    fun requestLocationUpdates() {
        service?.requestLocationUpdates()
    }

    fun stopLocationUpdates() {
        service?.removeLocationUpdates()
    }

    fun stopService() {
        service?.stopSelf()
    }

    fun getLocationPoints(): List<LatLng>? = service?.locationPoints
    fun getSpeeds(): List<Double>? = service?.speeds
    fun getDistance(): Double? = service?.distance
    fun getDurationInMillis(): Long? = service?.passedTime
}