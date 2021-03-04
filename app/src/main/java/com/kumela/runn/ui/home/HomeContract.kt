package com.kumela.runn.ui.home

import com.kumela.mvx.mvp.core.MvpModel
import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView


interface HomeContract {
    interface View: MvpView

    interface Presenter: MvpPresenter<View>

    interface Model: MvpModel
}