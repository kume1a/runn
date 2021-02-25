package com.kumela.runn.ui.onboarding

import com.kumela.runn.di.annotations.ScreenScope
import dagger.Subcomponent
import dagger.android.AndroidInjector

@ScreenScope
@Subcomponent(modules = [
    OnboardingModule::class
])
interface OnboardingScreenComponent: AndroidInjector<OnboardingController> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<OnboardingController>() {
        override fun seedInstance(instance: OnboardingController?) {}
    }
}