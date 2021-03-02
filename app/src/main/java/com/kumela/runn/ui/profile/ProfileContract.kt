package com.kumela.runn.ui.profile

import com.kumela.runn.ui.core.mvp.core.MvpPresenter
import com.kumela.runn.ui.core.mvp.core.MvpView

interface ProfileContract {

    interface View : MvpView

    interface Presenter: MvpPresenter<View>

}