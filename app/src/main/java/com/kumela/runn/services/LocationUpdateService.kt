package com.kumela.runn.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.kumela.runn.BuildConfig
import com.kumela.runn.core.base.BaseService
import com.kumela.runn.core.events.LocationEvent
import com.kumela.runn.core.events.RunSessionInfoEvent
import com.kumela.runn.core.events.RunSessionTick
import com.kumela.runn.data.managers.RequestingLocationManager
import com.kumela.runn.helpers.SimpleTimer
import com.kumela.runn.helpers.calculators.DistanceCalculator
import com.kumela.runn.helpers.calculators.SpeedCalculator
import com.kumela.runn.models.Duration
import com.kumela.runn.notifications.AppNotificationManager
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class LocationUpdateService : BaseService() {

    @Inject lateinit var requestingLocationManager: RequestingLocationManager
    @Inject lateinit var appNotificationManager: AppNotificationManager
    @Inject lateinit var distanceCalculator: DistanceCalculator
    @Inject lateinit var speedCalculator: SpeedCalculator

    private val binder = LocalBinder()
    private var changingConfigurations = false

    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var serviceHandler: Handler

    private var location: Location? = null
    private var distance: Double = 0.0
    private var lastTimestamp: Long = System.currentTimeMillis()
    private val speeds = mutableListOf<Double>()
    private val timer = object: SimpleTimer() {
        override fun onTick(duration: Duration) {
            EventBus.getDefault().post(RunSessionTick(duration))
        }
    }

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
        timer.resume()
        requestingLocationManager.setRequestingLocationUpdates(true)
        startService(Intent(applicationContext, LocationUpdateService::class.java))
        location = null
        try {
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, serviceHandler.looper)
        } catch (e: SecurityException) {
            requestingLocationManager.setRequestingLocationUpdates(false)
            Timber.e(e)
        }
    }

    fun removeLocationUpdates() {
        timer.pause()
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            requestingLocationManager.setRequestingLocationUpdates(false)
        } catch (e: SecurityException) {
            requestingLocationManager.setRequestingLocationUpdates(true)
            Timber.e(e)
        }
    }

    private fun onNewLocation(currentLocation: Location) {
        location?.let { loc ->
            distance += distanceCalculator.calculateDistance(loc, currentLocation)

            val lastLatLng = LatLng(loc.latitude, loc.longitude)
            val currentLatLng = LatLng(currentLocation.latitude, currentLocation.longitude)
            val bearing = currentLocation.bearing
            val timeInMillis = System.currentTimeMillis() - lastTimestamp
            val speed = speedCalculator.calculateSpeed(lastLatLng, currentLatLng, timeInMillis)

            speeds.add(speed)

            val averageSpeed = speeds.average()

            EventBus.getDefault().post(LocationEvent(lastLatLng, currentLatLng, bearing))
            EventBus.getDefault().post(RunSessionInfoEvent(distance, speed, averageSpeed, timer.passedTime))
        }
        location = currentLocation
        lastTimestamp = System.currentTimeMillis()

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
        private const val UPDATE_INTERVAL = 3000L
        private const val UPDATE_INTERVAL_FASTEST = UPDATE_INTERVAL / 2

        const val ACTION_BROADCAST = "${BuildConfig.APPLICATION_ID}.broadcast"

        const val EXTRA_LOCATION = "${BuildConfig.APPLICATION_ID}.location"
        const val EXTRA_DISTANCE = "${BuildConfig.APPLICATION_ID}.distance"
    }
}