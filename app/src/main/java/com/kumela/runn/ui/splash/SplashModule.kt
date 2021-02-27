package com.kumela.runn.ui.splash

import com.kumela.runn.data.db.user.UserService
import com.kumela.runn.di.annotations.ScreenScope
import com.kumela.runn.ui.core.navigation.ScreenNavigator
import dagger.Module
import dagger.Provides

@Module
object SplashModule {

    @JvmStatic
    @Provides
    @ScreenScope
    fun providePresenter(
        screenNavigator: ScreenNavigator,
        model: SplashContract.Model,
    ): SplashContract.Presenter = SplashPresenter(screenNavigator, model)

    @JvmStatic
    @Provides
    @ScreenScope
    fun provideSplashModel(
        userService: UserService,
    ): SplashContract.Model = SplashModel(userService)
}