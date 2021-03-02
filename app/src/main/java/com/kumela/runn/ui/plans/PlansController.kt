package com.kumela.runn.ui.plans

import com.kumela.runn.R
import com.kumela.runn.core.base.BaseController

class PlansController : BaseController<PlansContract.View, PlansContract.Presenter>(),
    PlansContract.View {
    override val layoutRes: Int
        get() = R.layout.screen_plans

    companion object {
        @JvmStatic
        fun newInstance(): PlansController {
            return PlansController()
        }
    }
}