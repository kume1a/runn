package com.kumela.runn.ui.onboarding

import android.util.Log
import com.kumela.runn.core.Constants
import com.kumela.runn.core.enums.Gender
import com.kumela.runn.di.annotations.ScreenScope
import com.kumela.runn.ui.core.mvp.SavedStatePresenter
import javax.inject.Inject

@ScreenScope
class OnboardingPresenter @Inject constructor() :
    SavedStatePresenter<OnboardingContract.View, OnboardingPresenter.SavedState>(SavedState()),
    OnboardingContract.Presenter {

    data class SavedState(
        val page: Int = 0,
        val gender: Gender = Gender.FEMALE,
        val weight: Int = Constants.MIN_WEIGHT,
        val height: Int = Constants.MIN_HEIGHT,
    )

    private var page = 0

    override fun onBackClicked() {
        page--
        view?.pageIndex = page

        if (page == PAGE_GENDER) view?.hideBackButton()
        if (page == PAGE_WEIGHT) view?.changeFinishButtonToNext()

        when (page) {
            PAGE_GENDER -> {
                view?.hideWeightGroup()
                view?.showGenderGroup()
            }
            PAGE_WEIGHT -> {
                view?.hideHeightGroup()
                view?.showWeightGroup()
            }
        }
    }

    override fun onNextClicked() {
        if (page != PAGE_HEIGHT) {
            page++
            view?.pageIndex = page
        } else {
            ifViewAttached { view ->
                val gender = view.gender
                val weight = view.weight
                val height = view.height

                Log.d(javaClass.simpleName,
                    "onNextClicked: gender = $gender, weight = $weight, height = $height")
            }
            return
        }

        if (page == PAGE_HEIGHT) view?.changeNextButtonToFinish()
        if (page == PAGE_WEIGHT) view?.showBackButton()

        when (page) {
            PAGE_WEIGHT -> {
                view?.hideGenderGroup()
                view?.showWeightGroup()
            }
            PAGE_HEIGHT -> {
                view?.hideWeightGroup()
                view?.showHeightGroup()
            }
        }
    }

    override fun latestState(): SavedState {
        return if (view != null) SavedState(
            page = view!!.pageIndex,
            gender = view!!.gender,
            weight = view!!.weight,
            height = view!!.height
        ) else SavedState()
    }

    override fun onRestoreState(state: SavedState) {
        ifViewAttached { view ->
            when (state.page) {
                PAGE_GENDER -> view.showGenderGroup()
                PAGE_WEIGHT -> {
                    view.showWeightGroup()
                    view.showBackButton()
                }
                PAGE_HEIGHT -> {
                    view.showHeightGroup()
                    view.showBackButton()
                    view.changeNextButtonToFinish()
                }
            }

            view.pageIndex = state.page
            view.gender = state.gender
            view.weight = state.weight
            view.height = state.height
        }
    }

    companion object {
        private const val PAGE_GENDER = 0
        private const val PAGE_WEIGHT = 1
        private const val PAGE_HEIGHT = 2
    }
}