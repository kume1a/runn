package com.kumela.runn.ui.splash

import com.kumela.mvx.mvp.core.MvpModel
import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView
import com.kumela.runn.data.db.user.User
import io.reactivex.rxjava3.core.Maybe

interface SplashContract {
    interface View : MvpView

    interface Presenter: MvpPresenter<View>

    interface Model: MvpModel {
        fun getUser(): Maybe<User>
    }
}