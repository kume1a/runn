package com.kumela.runn.ui.statistics

import com.kumela.runn.di.annotations.ScreenScope
import com.kumela.runn.di.common.ScreenModule
import dagger.Subcomponent
import dagger.android.AndroidInjector

@ScreenScope
@Subcomponent(modules = [ScreenModule::class, StatisticsModule::class])
interface StatisticsScreenComponent : AndroidInjector<StatisticsController> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<StatisticsController>() {
        override fun seedInstance(instance: StatisticsController?) {}
    }
}