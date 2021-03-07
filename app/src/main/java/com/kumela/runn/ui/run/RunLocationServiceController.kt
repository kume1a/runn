package com.kumela.runn.ui.run

import android.app.Activity
import android.content.*
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.kumela.runn.core.lifecycle.ScreenLifecycleTask
import com.kumela.runn.di.annotations.ScreenScope
import com.kumela.runn.services.LocationBroadcastReceiver
import com.kumela.runn.services.LocationUpdateService
import javax.inject.Inject

@ScreenScope
class RunLocationServiceController @Inject constructor() : ScreenLifecycleTask() {

    private lateinit var receiver: LocationBroadcastReceiver
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

        receiver = LocationBroadcastReceiver()
        activity?.bindService(Intent(activity, LocationUpdateService::class.java), mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onActivityStarted(activity: Activity) {
        super.onActivityStarted(activity)
        activity.bindService(Intent(activity, LocationUpdateService::class.java), mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, IntentFilter(LocationUpdateService.ACTION_BROADCAST))
    }

    override fun onActivityPaused(activity: Activity) {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver)
        super.onActivityPaused(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        if (bound) {
            activity.unbindService(mServiceConnection)
            bound = false
        }
        super.onActivityStopped(activity)
    }

    fun startService() {
        service?.requestLocationUpdates()
    }

    fun endService() {
        service?.removeLocationUpdates()
    }
}