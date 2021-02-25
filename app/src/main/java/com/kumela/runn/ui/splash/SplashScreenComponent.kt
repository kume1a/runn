package com.kumela.runn.ui.splash

import com.kumela.runn.di.annotations.ScreenScope
import dagger.Subcomponent
import dagger.android.AndroidInjector

@ScreenScope
@Subcomponent
interface SplashScreenComponent: AndroidInjector<SplashController> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<SplashController>() {
        override fun seedInstance(instance: SplashController?) {}
    }
}