package com.kumela.runn.core.main

import com.kumela.runn.di.annotations.ActivityScope
import com.kumela.runn.di.common.MainScreenBindingModule
import com.kumela.runn.helpers.HelpersModule
import com.kumela.runn.ui.core.navigation.NavigationModule
import dagger.Subcomponent
import dagger.android.AndroidInjector

@ActivityScope
@Subcomponent(
    modules = [
        MainScreenBindingModule::class,
        NavigationModule::class,
        HelpersModule::class,
    ]
)
interface MainActivityComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>() {
        override fun seedInstance(instance: MainActivity?) {}
    }
}