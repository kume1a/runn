package com.kumela.runn.ui.onboarding

import com.kumela.mvx.mvp.core.MvpModel
import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView
import com.kumela.runn.data.db.user.User
import com.kumela.views.enums.Gender
import io.reactivex.rxjava3.core.Completable


interface OnboardingContract {

    interface View: MvpView {
        fun showGenderGroup()
        fun hideGenderGroup()
        var gender: Gender

        fun showWeightGroup()
        fun hideWeightGroup()
        var weight: Int

        fun showHeightGroup()
        fun hideHeightGroup()
        var height: Int

        var pageIndex: Int
        fun hideBackButton()
        fun showBackButton()
        fun changeNextButtonToFinish()
        fun changeFinishButtonToNext()

        fun showProgressIndication()
        fun hideProgressIndication()
    }

    interface Presenter: MvpPresenter<View> {
        fun onBackClicked()
        fun onNextClicked()
    }

    interface Model: MvpModel {
        fun createUser(user: User): Completable
    }
}