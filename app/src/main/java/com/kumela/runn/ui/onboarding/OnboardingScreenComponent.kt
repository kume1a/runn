package com.kumela.runn.ui.onboarding

import com.kumela.runn.di.annotations.ScreenScope
import com.kumela.runn.di.common.ScreenModule
import dagger.Subcomponent
import dagger.android.AndroidInjector

@ScreenScope
@Subcomponent(modules = [ScreenModule::class, OnboardingModule::class])
interface OnboardingScreenComponent : AndroidInjector<OnboardingController> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<OnboardingController>() {
        override fun seedInstance(instance: OnboardingController?) {}
    }
}