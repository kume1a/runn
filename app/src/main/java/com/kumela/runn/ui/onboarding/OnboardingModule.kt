package com.kumela.runn.ui.onboarding
import com.kumela.runn.data.db.user.UserService
import com.kumela.runn.di.annotations.ScreenScope
import com.kumela.runn.ui.core.navigation.ScreenNavigator
import dagger.Module
import dagger.Provides

@Module
object OnboardingModule {

    @JvmStatic
    @Provides
    @ScreenScope
    fun provideOnboardingPresenter(
        model: OnboardingContract.Model,
        screenNavigator: ScreenNavigator,
    ): OnboardingContract.Presenter = OnboardingPresenter(model, screenNavigator)

    @JvmStatic
    @Provides
    @ScreenScope
    fun provideOnboardingModel(userService: UserService): OnboardingContract.Model =
        OnboardingModel(userService)
}