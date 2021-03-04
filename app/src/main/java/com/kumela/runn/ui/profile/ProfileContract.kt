package com.kumela.runn.ui.profile

import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView


interface ProfileContract {

    interface View : MvpView

    interface Presenter: MvpPresenter<View>

}