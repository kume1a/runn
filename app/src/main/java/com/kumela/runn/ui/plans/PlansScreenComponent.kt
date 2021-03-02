package com.kumela.runn.ui.plans

import com.kumela.runn.di.annotations.ScreenScope
import dagger.Subcomponent
import dagger.android.AndroidInjector

@ScreenScope
@Subcomponent(modules = [PlansModule::class])
interface PlansScreenComponent: AndroidInjector<PlansController> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<PlansController>() {
        override fun seedInstance(instance: PlansController?) {}
    }
}