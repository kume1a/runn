package com.kumela.runn.di.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import dagger.Module
import dagger.Provides

@Module
class ServiceModule(private val service: Service) {

    @Provides
    fun provideContext(): Context = service

    @Provides
    fun provideNotificationManager(context: Context): NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}