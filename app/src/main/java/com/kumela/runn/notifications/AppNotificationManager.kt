package com.kumela.runn.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.kumela.runn.BuildConfig
import com.kumela.runn.R
import com.kumela.runn.services.LocationUpdateService
import com.kumela.runn.core.main.MainActivity
import javax.inject.Inject

class AppNotificationManager @Inject constructor(private val notificationManager: NotificationManager) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                LOCATION_UPDATES_CHANNEL_ID,
                BuildConfig.APPLICATION_ID,
                NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendLocationNotification(context: Context, contentText: String, contentTitle: String) {
        notificationManager.notify(LOCATION_UPDATES_NOTIFICATION_ID,
            getLocationNotification(context, contentText, contentTitle))
    }

    fun getLocationNotification(context: Context, contentText: String, contentTitle: String): Notification {
        val serviceIntent = Intent(context, LocationUpdateService::class.java)
        serviceIntent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)

        val activityIntent = Intent(context, MainActivity::class.java)
        activityIntent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)

        val servicePendingIntent = PendingIntent.getService(context, 1, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val activityPendingIntent = PendingIntent.getActivity(context, 2, activityIntent, 0)

        @Suppress("DEPRECATION")
        val importance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            NotificationManager.IMPORTANCE_LOW else Notification.PRIORITY_LOW

        val builder = NotificationCompat.Builder(context, LOCATION_UPDATES_CHANNEL_ID)
            .addAction(R.drawable.ic_close, "stop", servicePendingIntent)
            .setContentIntent(activityPendingIntent)
            .setContentText(contentText)
            .setContentTitle(contentTitle)
            .setOngoing(true)
            .setPriority(importance)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setWhen(System.currentTimeMillis())

        return builder.build()
    }

    companion object {
        const val EXTRA_STARTED_FROM_NOTIFICATION = "${BuildConfig.APPLICATION_ID}.started_from_notification"

        private const val LOCATION_UPDATES_CHANNEL_ID = "location channel"
        const val LOCATION_UPDATES_NOTIFICATION_ID = 1
    }
}