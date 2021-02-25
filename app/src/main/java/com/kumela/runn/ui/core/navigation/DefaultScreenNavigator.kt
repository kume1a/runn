package com.kumela.runn.ui.core.navigation

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.kumela.runn.ui.onboarding.OnboardingController
import javax.inject.Inject

class DefaultScreenNavigator @Inject constructor() : ScreenNavigator {

    private var router: Router? = null

    override fun pop(): Boolean {
        return router != null && router!!.handleBack()
    }

    override fun initializeWithRouter(router: Router, initialScreen: Controller) {
        this.router = router
        if (!this.router!!.hasRootController()) {
            this.router!!.setRoot(RouterTransaction.with(initialScreen))
        }
    }

    override fun dispose() {
        router = null
    }

    override fun toOnboarding() {
        router?.replaceTopController(
            RouterTransaction.with(OnboardingController.newInstance())
                .pushChangeHandler(FadeChangeHandler())
        )
    }
}