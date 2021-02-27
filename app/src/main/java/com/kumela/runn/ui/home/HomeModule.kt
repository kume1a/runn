package com.kumela.runn.ui.home

import com.kumela.runn.di.annotations.ScreenScope
import dagger.Module
import dagger.Provides

@Module
object HomeModule {

    @JvmStatic
    @Provides
    @ScreenScope
    fun provideHomePresenter(): HomeContract.Presenter = HomePresenter()

    @JvmStatic
    @Provides
    @ScreenScope
    fun provideHomeModel(): HomeContract.Model = HomeModel()
}