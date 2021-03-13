package com.kumela.runn.ui.core.navigation

import android.os.Looper
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.kumela.dialogcontroller.dialogs.AlertDialog
import com.kumela.dialogcontroller.pushDialog
import com.kumela.runn.ui.home.HomeController
import com.kumela.runn.ui.onboarding.OnboardingController
import com.kumela.runn.ui.plans.PlansController
import com.kumela.runn.ui.profile.ProfileController
import com.kumela.runn.ui.run.RunController
import com.kumela.runn.ui.statistics.StatisticsController
import android.os.Handler
import javax.inject.Inject

class DefaultScreenNavigator @Inject constructor() : ScreenNavigator {

    private var router: Router? = null
    private val fadeChangeHandler = FadeChangeHandler()
    private val handler = Handler(Looper.getMainLooper())

    override fun pop(): Boolean {
        return router != null && router!!.handleBack()
    }

    override fun popFromDialog() {
        pop()
        handler.postDelayed({pop()}, 200)
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
                .pushChangeHandler(fadeChangeHandler)
        )
    }

    override fun toHome() {
        router?.replaceTopController(
            RouterTransaction.with(HomeController.newInstance())
                .pushChangeHandler(fadeChangeHandler)
                .popChangeHandler(fadeChangeHandler)
        )
    }

    override fun toPlans() {
        router?.replaceTopController(
            RouterTransaction.with(PlansController.newInstance())
                .pushChangeHandler(fadeChangeHandler)
                .popChangeHandler(fadeChangeHandler)
        )
    }

    override fun toStatistics() {
        router?.replaceTopController(
            RouterTransaction.with(StatisticsController.newInstance())
                .pushChangeHandler(fadeChangeHandler)
                .popChangeHandler(fadeChangeHandler)
        )
    }

    override fun toProfile() {
        router?.replaceTopController(
            RouterTransaction.with(ProfileController.newInstance())
                .pushChangeHandler(fadeChangeHandler)
                .popChangeHandler(fadeChangeHandler)
        )
    }

    override fun toRun() {
        router?.pushController(
            RouterTransaction.with(RunController.newInstance())
                .pushChangeHandler(fadeChangeHandler)
                .popChangeHandler(fadeChangeHandler)
        )
    }

    override fun showAlertDialog(alertDialog: AlertDialog) {
        router?.pushDialog(alertDialog)
    }
}