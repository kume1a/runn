package com.kumela.runn.di.common;

import com.bluelinelabs.conductor.Controller;
import com.kumela.runn.di.annotations.ControllerKey;
import com.kumela.runn.ui.home.HomeController;
import com.kumela.runn.ui.home.HomeScreenComponent;
import com.kumela.runn.ui.onboarding.OnboardingController;
import com.kumela.runn.ui.onboarding.OnboardingScreenComponent;
import com.kumela.runn.ui.plans.PlansController;
import com.kumela.runn.ui.plans.PlansScreenComponent;
import com.kumela.runn.ui.profile.ProfileController;
import com.kumela.runn.ui.profile.ProfileScreenComponent;
import com.kumela.runn.ui.run.RunController;
import com.kumela.runn.ui.run.RunScreenComponent;
import com.kumela.runn.ui.splash.SplashController;
import com.kumela.runn.ui.splash.SplashScreenComponent;
import com.kumela.runn.ui.statistics.StatisticsController;
import com.kumela.runn.ui.statistics.StatisticsScreenComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {
        SplashScreenComponent.class,
        OnboardingScreenComponent.class,
        HomeScreenComponent.class,
        PlansScreenComponent.class,
        StatisticsScreenComponent.class,
        ProfileScreenComponent.class,
        RunScreenComponent.class,
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

    @Binds
    @IntoMap
    @ControllerKey(HomeController.class)
    abstract AndroidInjector.Factory<? extends Controller> bindHomeScreenInjector(HomeScreenComponent.Builder builder);

    @Binds
    @IntoMap
    @ControllerKey(PlansController.class)
    abstract AndroidInjector.Factory<? extends Controller> bindPlansScreenInjector(PlansScreenComponent.Builder builder);

    @Binds
    @IntoMap
    @ControllerKey(StatisticsController.class)
    abstract AndroidInjector.Factory<? extends Controller> bindStatisticsScreenInjector(StatisticsScreenComponent.Builder builder);

    @Binds
    @IntoMap
    @ControllerKey(ProfileController.class)
    abstract AndroidInjector.Factory<? extends Controller> bindProfileScreenInjector(ProfileScreenComponent.Builder builder);

    @Binds
    @IntoMap
    @ControllerKey(RunController.class)
    abstract AndroidInjector.Factory<? extends Controller> bindRunScreenInjector(RunScreenComponent.Builder builder);
}
