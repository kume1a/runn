package com.kumela.runn.ui.profile

import com.kumela.runn.di.annotations.ScreenScope
import com.kumela.runn.di.common.ScreenModule
import dagger.Subcomponent
import dagger.android.AndroidInjector

@ScreenScope
@Subcomponent(modules = [ScreenModule::class, ProfileModule::class])
interface ProfileScreenComponent : AndroidInjector<ProfileController> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<ProfileController>() {
        override fun seedInstance(instance: ProfileController?) {}
    }
}