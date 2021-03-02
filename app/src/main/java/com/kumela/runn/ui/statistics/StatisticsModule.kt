package com.kumela.runn.ui.statistics

import com.kumela.runn.di.annotations.ScreenScope
import dagger.Module
import dagger.Provides

@Module
object StatisticsModule {

    @JvmStatic
    @Provides
    @ScreenScope
    fun provideStatisticsPresenter(): StatisticsContract.Presenter = StatisticsPresenter()
}