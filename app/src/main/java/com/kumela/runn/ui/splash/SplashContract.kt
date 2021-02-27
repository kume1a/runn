package com.kumela.runn.ui.splash

import com.kumela.runn.data.db.user.User
import com.kumela.runn.ui.core.mvp.MvpModel
import com.kumela.runn.ui.core.mvp.core.MvpPresenter
import com.kumela.runn.ui.core.mvp.core.MvpView
import io.reactivex.rxjava3.core.Maybe

interface SplashContract {
    interface View : MvpView

    interface Presenter: MvpPresenter<View>

    interface Model: MvpModel {
        fun getUser(): Maybe<User>
    }
}