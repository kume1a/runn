package com.kumela.runn.ui.navigation

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router

interface ScreenNavigator {
    fun pop(): Boolean

    fun initializeWithRouter(router: Router, initialScreen: Controller)

    fun dispose()
}