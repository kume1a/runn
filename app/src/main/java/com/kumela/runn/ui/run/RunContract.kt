package com.kumela.runn.ui.run

import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView


interface RunContract {

    interface View: MvpView

    interface Presenter: MvpPresenter<View>

}