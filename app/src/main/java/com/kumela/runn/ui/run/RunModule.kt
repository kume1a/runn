package com.kumela.runn.ui.run

import com.kumela.runn.di.annotations.ScreenScope
import dagger.Module
import dagger.Provides

@Module
object RunModule {

    @JvmStatic
    @Provides
    @ScreenScope
    fun provideRunPresenter(): RunContract.Presenter = RunPresenter()
}