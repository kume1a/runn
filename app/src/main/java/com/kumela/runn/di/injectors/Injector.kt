package com.kumela.runn.di.injectors

import android.app.Activity
import com.bluelinelabs.conductor.Controller

object Injector {

    @JvmStatic
    fun inject(activity: Activity) {
        ActivityInjector.get(activity).inject(activity)
    }

    @JvmStatic
    fun clear(activity: Activity) {
        ActivityInjector.get(activity).clear(activity)
    }

    @JvmStatic
    fun inject(controller: Controller) {
        controller.activity?.let { activity ->
            ScreenInjector.get(activity).inject(controller)
        }
    }

    @JvmStatic
    fun clear(controller: Controller) {
        controller.activity?.let { activity ->
            ScreenInjector.get(activity).clear(controller)
        }
    }
}