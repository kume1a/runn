package com.kumela.runn.ui.home

import com.kumela.runn.ui.core.mvp.MvpModel
import com.kumela.runn.ui.core.mvp.core.MvpPresenter
import com.kumela.runn.ui.core.mvp.core.MvpView

interface HomeContract {
    interface View: MvpView

    interface Presenter: MvpPresenter<View>

    interface Model: MvpModel
}