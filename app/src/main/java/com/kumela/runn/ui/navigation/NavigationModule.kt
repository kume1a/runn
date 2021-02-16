package com.kumela.runn.ui.navigation

import dagger.Binds
import dagger.Module

@Module
abstract class NavigationModule {

    @Binds
    abstract fun bindScreenNavigator(screenNavigator: DefaultScreenNavigator): ScreenNavigator
}