package com.kumela.runn.ui.core.navigation

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.hippo.conductor.dialog.DialogController

interface ScreenNavigator {
    fun pop(): Boolean
    fun popFromDialog()

    fun showDialog(dialog: DialogController)

    fun initializeWithRouter(router: Router, initialScreen: Controller)
    fun dispose()

    fun toOnboarding()
    fun toHome()
    fun toPlans()
    fun toStatistics()
    fun toProfile()
    fun toRun()
}