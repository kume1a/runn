package com.kumela.runn.di.common;

import com.bluelinelabs.conductor.Controller;
import com.kumela.runn.di.annotations.ControllerKey;
import com.kumela.runn.ui.home.HomeController;
import com.kumela.runn.ui.home.HomeScreenComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(
        subcomponents = {
                HomeScreenComponent.class,
        }
)
public abstract class MainScreenBindingModule {

    @Binds
    @IntoMap
    @ControllerKey(HomeController.class)
    abstract AndroidInjector.Factory<? extends Controller> bindHomeScreenInjector(HomeScreenComponent.Builder builder);
}
