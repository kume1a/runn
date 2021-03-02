package com.kumela.runn.ui.run

import com.kumela.runn.ui.core.mvp.core.MvpPresenter
import com.kumela.runn.ui.core.mvp.core.MvpView

interface RunContract {

    interface View: MvpView

    interface Presenter: MvpPresenter<View>

}