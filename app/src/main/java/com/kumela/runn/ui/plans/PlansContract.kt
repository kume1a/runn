package com.kumela.runn.ui.plans

import com.kumela.mvx.mvp.core.MvpModel
import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView


interface PlansContract {

    interface View: MvpView

    interface Presenter: MvpPresenter<View>

    interface Model: MvpModel
}