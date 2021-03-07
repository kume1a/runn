package com.kumela.runn.di.service

import com.kumela.runn.services.LocationUpdateService
import dagger.Subcomponent

@Subcomponent(modules = [ServiceModule::class])
interface ServiceComponent {
    fun inject(locationUpdateService: LocationUpdateService)
}