package com.kumela.runn.ui.statistics

import com.kumela.runn.R
import com.kumela.runn.core.base.BaseController

class StatisticsController: BaseController<StatisticsContract.View, StatisticsContract.Presenter>(), StatisticsContract.View {

    override val layoutRes: Int
        get() = R.layout.screen_statistics

    companion object {
        @JvmStatic
        fun newInstance(): StatisticsController {
            return StatisticsController()
        }
    }
}