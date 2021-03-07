package com.kumela.runn.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.kumela.runn.BuildConfig
import com.kumela.runn.core.base.BaseService
import com.kumela.runn.data.managers.RequestingLocationManager
import com.kumela.runn.notifications.AppNotificationManager
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class LocationUpdateService : BaseService() {

    @Inject lateinit var requestingLocationManager: RequestingLocationManager
    @Inject lateinit var appNotificationManager: AppNotificationManager

    private val binder = LocalBinder()
    private var changingConfigurations = false

    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var serviceHandler: Handler
    private var location: Location? = null

    override fun onCreate() {
        injector.inject(this)
        super.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                onNewLocation(result.lastLocation)
            }
        }

        locationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL
            fastestInterval = UPDATE_INTERVAL_FASTEST
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        try {
            fusedLocationClient.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        location = task.result
                    } else {
                        Timber.w("Failed to get location")
                    }
                }
        } catch (e: SecurityException) {
            Timber.e(e)
        }

        val handlerThread = HandlerThread(javaClass.simpleName)
        handlerThread.start()
        serviceHandler = Handler(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val startedFromNotification = intent?.getBooleanExtra(AppNotificationManager.EXTRA_STARTED_FROM_NOTIFICATION, false) ?: false
        if (startedFromNotification) {
            removeLocationUpdates()
            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        changingConfigurations = true
    }

    override fun onBind(intent: Intent?): IBinder {
        stopForeground(true)
        changingConfigurations = false
        return binder
    }

    override fun onRebind(intent: Intent?) {
        stopForeground(true)
        changingConfigurations = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        if (!changingConfigurations && requestingLocationManager.requestingLocationUpdates()) {
            startForeground(
                AppNotificationManager.LOCATION_UPDATES_NOTIFICATION_ID,
                appNotificationManager.getLocationNotification(this, location.toString(), "title")
            )
        }

        return true
    }

    override fun onDestroy() {
        serviceHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    fun requestLocationUpdates() {
        requestingLocationManager.setRequestingLocationUpdates(true)
        startService(Intent(applicationContext, LocationUpdateService::class.java))
        try {
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        } catch (e: SecurityException) {
            requestingLocationManager.setRequestingLocationUpdates(false)
            Timber.e(e)
        }
    }

    fun removeLocationUpdates() {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            requestingLocationManager.setRequestingLocationUpdates(false)
        } catch (e: SecurityException) {
            requestingLocationManager.setRequestingLocationUpdates(true)
            Timber.e(e)
        }
    }

    private fun onNewLocation(lastLocation: Location) {
        location = lastLocation

        val intent = Intent(ACTION_BROADCAST).also { intent ->
            intent.putExtra(EXTRA_LOCATION, lastLocation)
        }
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

        if (serviceIsRunningInForeground(this)) {
            appNotificationManager.sendLocationNotification(this, location.toString(), "title")
        }
    }

    private fun serviceIsRunningInForeground(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        @Suppress("DEPRECATION")
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (javaClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    inner class LocalBinder : Binder() {
        fun getService(): LocationUpdateService = this@LocationUpdateService
    }

    companion object {
        private const val UPDATE_INTERVAL = 2500L
        private const val UPDATE_INTERVAL_FASTEST = UPDATE_INTERVAL / 2

        const val ACTION_BROADCAST = "${BuildConfig.APPLICATION_ID}.broadcast"
        const val EXTRA_LOCATION = "${BuildConfig.APPLICATION_ID}.location"
    }
}