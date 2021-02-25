package com.kumela.runn.ui.onboarding

import dagger.Binds
import dagger.Module

@Module
abstract class OnboardingModule {
    @Binds
    abstract fun bindPresenter(presenter: OnboardingPresenter): OnboardingContract.Presenter
}