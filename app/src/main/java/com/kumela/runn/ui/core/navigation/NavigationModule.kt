package com.kumela.runn.ui.core.navigation

import com.kumela.runn.di.annotations.ActivityScope
import dagger.Binds
import dagger.Module

@Module
abstract class NavigationModule {

    @ActivityScope
    @Binds
    abstract fun bindScreenNavigator(screenNavigator: DefaultScreenNavigator): ScreenNavigator
}