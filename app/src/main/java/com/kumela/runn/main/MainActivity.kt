package com.kumela.runn.main

import com.bluelinelabs.conductor.Controller
import com.kumela.runn.R
import com.kumela.runn.core.base.BaseActivity
import com.kumela.runn.ui.home.HomeController

class MainActivity : BaseActivity() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    override val initialScreen: Controller
        get() = HomeController()
}