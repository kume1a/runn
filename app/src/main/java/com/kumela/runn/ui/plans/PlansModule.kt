package com.kumela.runn.ui.plans

import com.kumela.runn.di.annotations.ScreenScope
import dagger.Module
import dagger.Provides

@Module
object PlansModule {

    @JvmStatic
    @Provides
    @ScreenScope
    fun providePlansPresenter(): PlansContract.Presenter = PlansPresenter()
}