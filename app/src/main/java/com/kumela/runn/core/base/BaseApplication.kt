package com.kumela.runn.core.base

import android.app.Application
import com.kumela.runn.di.app.AppComponent
import com.kumela.runn.di.app.AppModule
import com.kumela.runn.di.app.DaggerAppComponent
import com.kumela.runn.di.injectors.ActivityInjector
import javax.inject.Inject

class BaseApplication : Application() {

    lateinit var appComponent: AppComponent

    @Inject lateinit var activityInjector: ActivityInjector

    override fun onCreate() {
        super.onCreate()

        appComponent = createAppComponent()
        appComponent.inject(this)
    }

    protected fun createAppComponent(): AppComponent = DaggerAppComponent
        .builder()
        .appModule(AppModule(this))
        .build()


}