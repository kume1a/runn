package com.kumela.runn.ui.onboarding

import com.kumela.runn.core.enums.Gender
import com.kumela.runn.ui.core.mvp.core.MvpPresenter
import com.kumela.runn.ui.core.mvp.core.MvpView

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
    }

    interface Presenter: MvpPresenter<View> {
        fun onBackClicked()
        fun onNextClicked()
    }
}