package com.kumela.runn.core.base

import android.app.Application
import com.kumela.runn.BuildConfig
import com.kumela.runn.di.app.AppComponent
import com.kumela.runn.di.app.AppModule
import com.kumela.runn.di.app.DaggerAppComponent
import com.kumela.runn.di.injectors.ActivityInjector
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

class BaseApplication : Application() {

    lateinit var appComponent: AppComponent

    @Inject lateinit var activityInjector: ActivityInjector

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        RxJavaPlugins.setErrorHandler { throwable ->
            Timber.e(throwable)
        }

        appComponent = createAppComponent()
        appComponent.inject(this)
    }

    private fun createAppComponent(): AppComponent = DaggerAppComponent
        .builder()
        .appModule(AppModule(this))
        .build()
}