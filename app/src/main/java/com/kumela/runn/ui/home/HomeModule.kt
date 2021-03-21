package com.kumela.runn.ui.home

import com.kumela.runn.data.db.run.RunSessionService
import com.kumela.runn.di.annotations.ScreenScope
import dagger.Module
import dagger.Provides

@Module
object HomeModule {

    @JvmStatic
    @Provides
    @ScreenScope
    fun provideHomePresenter(
        runSessionService: RunSessionService,
    ): HomeContract.Presenter =
        HomePresenter(runSessionService)
}