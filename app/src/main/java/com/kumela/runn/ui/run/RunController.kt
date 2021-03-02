package com.kumela.runn.ui.run

import com.kumela.runn.R
import com.kumela.runn.core.base.BaseController

class RunController: BaseController<RunContract.View, RunContract.Presenter>(), RunContract.View {

    override val layoutRes: Int
        get() = R.layout.screen_run

    companion object {
        fun newInstance(): RunController {
            return RunController()
        }
    }
}