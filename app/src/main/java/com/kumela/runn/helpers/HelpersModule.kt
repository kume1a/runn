package com.kumela.runn.helpers

import dagger.Binds
import dagger.Module

@Module
abstract class HelpersModule {

    @Binds
    abstract fun bindPermissionHandler(defaultPermissionHandler: DefaultPermissionHandler): PermissionHandler
}