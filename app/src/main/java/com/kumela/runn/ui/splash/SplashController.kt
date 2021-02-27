package com.kumela.runn.ui.splash

import com.kumela.runn.R
import com.kumela.runn.core.base.BaseController

class SplashController : BaseController<SplashContract.View, SplashContract.Presenter>() {

    override val layoutRes: Int
        get() = R.layout.screen_splash
}