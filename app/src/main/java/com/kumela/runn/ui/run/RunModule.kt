package com.kumela.runn.ui.run

import com.kumela.runn.core.lifecycle.ScreenLifecycleTask
import com.kumela.runn.di.annotations.ScreenScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
abstract class RunModule {

    @Binds
    @IntoSet
    abstract fun bindRunLocationServiceTask(runLocationServiceController: RunLocationServiceController): ScreenLifecycleTask

    companion object {

        @JvmStatic
        @Provides
        @ScreenScope
        fun provideRunPresenter(runLocationServiceController: RunLocationServiceController, ): RunContract.Presenter =
            RunPresenter(runLocationServiceController)
    }
}