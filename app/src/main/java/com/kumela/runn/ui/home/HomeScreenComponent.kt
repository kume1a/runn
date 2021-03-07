package com.kumela.runn.ui.home

import com.kumela.runn.di.annotations.ScreenScope
import com.kumela.runn.di.common.ScreenModule
import dagger.Subcomponent
import dagger.android.AndroidInjector

@ScreenScope
@Subcomponent(modules = [ScreenModule::class, HomeModule::class])
interface HomeScreenComponent : AndroidInjector<HomeController> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<HomeController>() {
        override fun seedInstance(instance: HomeController?) {}
    }
}