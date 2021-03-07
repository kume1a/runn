package com.kumela.runn.ui.run

import com.kumela.runn.di.annotations.ScreenScope
import com.kumela.runn.di.common.ScreenModule
import dagger.Subcomponent
import dagger.android.AndroidInjector

@ScreenScope
@Subcomponent(modules = [ScreenModule::class, RunModule::class])
interface RunScreenComponent: AndroidInjector<RunController> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<RunController>() {
        override fun seedInstance(instance: RunController?) {}
    }
}