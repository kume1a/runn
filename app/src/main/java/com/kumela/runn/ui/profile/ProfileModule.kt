package com.kumela.runn.ui.profile

import com.kumela.runn.di.annotations.ScreenScope
import dagger.Module
import dagger.Provides

@Module
object ProfileModule {

    @JvmStatic
    @Provides
    @ScreenScope
    fun provideProfilePresenter(): ProfileContract.Presenter = ProfilePresenter()
}