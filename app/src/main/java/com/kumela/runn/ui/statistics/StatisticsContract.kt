package com.kumela.runn.ui.statistics

import com.kumela.runn.ui.core.mvp.core.MvpModel
import com.kumela.runn.ui.core.mvp.core.MvpPresenter
import com.kumela.runn.ui.core.mvp.core.MvpView

interface StatisticsContract {

    interface View: MvpView

    interface Presenter: MvpPresenter<View>

    interface Model: MvpModel
}