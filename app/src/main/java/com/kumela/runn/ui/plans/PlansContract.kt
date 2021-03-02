package com.kumela.runn.ui.plans

import com.kumela.runn.ui.core.mvp.core.MvpModel
import com.kumela.runn.ui.core.mvp.core.MvpPresenter
import com.kumela.runn.ui.core.mvp.core.MvpView

interface PlansContract {

    interface View: MvpView

    interface Presenter: MvpPresenter<View>

    interface Model: MvpModel
}