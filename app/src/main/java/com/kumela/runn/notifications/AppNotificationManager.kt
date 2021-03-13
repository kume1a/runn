package com.kumela.runn.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.kumela.runn.BuildConfig
import com.kumela.runn.R
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

    fun sendLocationNotification(context: Context, textDuration: String, textDistance: String) {
        notificationManager.notify(LOCATION_UPDATES_NOTIFICATION_ID,
            getLocationNotification(context, textDuration, textDistance))
    }

    fun getLocationNotification(context: Context, textDuration: String, textDistance: String): Notification {
        val activityIntent = Intent(context, MainActivity::class.java)
        activityIntent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)

        val activityPendingIntent = PendingIntent.getActivity(context, 2, activityIntent, 0)

        val contentView = RemoteViews(context.packageName, R.layout.notification_run)
        contentView.setTextViewText(R.id.text_duration, textDuration)
        contentView.setTextViewText(R.id.text_distance, textDistance)

        @Suppress("DEPRECATION")
        val importance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            NotificationManager.IMPORTANCE_LOW else Notification.PRIORITY_LOW

        val builder = NotificationCompat.Builder(context, LOCATION_UPDATES_CHANNEL_ID)
            .setContentIntent(activityPendingIntent)
            .setCustomBigContentView(contentView)
            // .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setOngoing(true)
            .setPriority(importance)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setWhen(System.currentTimeMillis())

        return builder.build()
    }

    companion object {
        const val EXTRA_STARTED_FROM_NOTIFICATION = "${BuildConfig.APPLICATION_ID}.started_from_notification"

        private const val LOCATION_UPDATES_CHANNEL_ID = "location channel"
        const val LOCATION_UPDATES_NOTIFICATION_ID = 1
    }
}