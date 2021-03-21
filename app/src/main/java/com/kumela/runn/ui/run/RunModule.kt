package com.kumela.runn.ui.run

import com.kumela.runn.core.lifecycle.ScreenLifecycleTask
import com.kumela.runn.data.db.run.RunSessionService
import com.kumela.runn.data.db.user.UserService
import com.kumela.runn.di.annotations.ScreenScope
import com.kumela.runn.helpers.calculators.BurnedCalorieCalculator
import com.kumela.runn.ui.core.navigation.ScreenNavigator
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
        fun provideRunPresenter(
            runLocationServiceController: RunLocationServiceController,
            burnedCalorieCalculator: BurnedCalorieCalculator,
            screenNavigator: ScreenNavigator,
            userService: UserService,
            runSessionService: RunSessionService,
        ): RunContract.Presenter =
            RunPresenter(
                runLocationServiceController,
                burnedCalorieCalculator,
                screenNavigator,
                userService,
                runSessionService
            )
    }
}