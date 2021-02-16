package com.kumela.runn.main

import com.kumela.runn.di.annotations.ActivityScope
import com.kumela.runn.di.common.MainScreenBindingModule
import com.kumela.runn.ui.navigation.NavigationModule
import dagger.Subcomponent
import dagger.android.AndroidInjector

@ActivityScope
@Subcomponent(
    modules = [
        MainScreenBindingModule::class,
        NavigationModule::class,
    ]
)
interface MainActivityComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>() {
        override fun seedInstance(instance: MainActivity?) {}
    }
}