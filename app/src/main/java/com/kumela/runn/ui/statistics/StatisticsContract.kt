package com.kumela.runn.ui.statistics

import com.kumela.mvx.mvp.core.MvpModel
import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView


interface StatisticsContract {

    interface View: MvpView

    interface Presenter: MvpPresenter<View>

    interface Model: MvpModel
}