package com.kumela.runn.di.common;

import com.bluelinelabs.conductor.Controller;
import com.kumela.runn.di.annotations.ControllerKey;
import com.kumela.runn.ui.onboarding.OnboardingController;
import com.kumela.runn.ui.onboarding.OnboardingScreenComponent;
import com.kumela.runn.ui.splash.SplashController;
import com.kumela.runn.ui.splash.SplashScreenComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {
        SplashScreenComponent.class,
        OnboardingScreenComponent.class,
})
public abstract class MainScreenBindingModule {

    @Binds
    @IntoMap
    @ControllerKey(SplashController.class)
    abstract AndroidInjector.Factory<? extends Controller> bindSplashScreenInjector(SplashScreenComponent.Builder builder);

    @Binds
    @IntoMap
    @ControllerKey(OnboardingController.class)
    abstract AndroidInjector.Factory<? extends Controller> bindOnboardingScreenInjector(OnboardingScreenComponent.Builder builder);
}
