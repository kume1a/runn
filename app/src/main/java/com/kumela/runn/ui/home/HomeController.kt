package com.kumela.runn.ui.home

import com.kumela.runn.R
import com.kumela.runn.core.base.BaseController

class HomeController : BaseController<HomeContract.View, HomeContract.Presenter>() {
    override val layoutRes: Int
        get() = R.layout.screen_home

    companion object {
        @JvmStatic
        fun newInstance(): HomeController {
            return HomeController()
        }
    }
}