package com.kumela.runn.di.app

import com.kumela.runn.core.base.BaseApplication
import com.kumela.runn.di.common.ActivityBindingModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ActivityBindingModule::class
    ]
)
interface AppComponent {
    fun inject(app: BaseApplication)
}