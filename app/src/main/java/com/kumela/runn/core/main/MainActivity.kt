package com.kumela.runn.core.main

import com.bluelinelabs.conductor.Controller
import com.kumela.runn.R
import com.kumela.runn.core.base.BaseActivity
import com.kumela.runn.ui.splash.SplashController

class MainActivity : BaseActivity() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    override val initialScreen: Controller
        get() = SplashController()
}