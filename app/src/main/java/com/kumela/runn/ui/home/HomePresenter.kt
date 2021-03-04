package com.kumela.runn.ui.home

import com.kumela.mvx.mvp.SavedStatePresenter


class HomePresenter : SavedStatePresenter<HomeContract.View, HomePresenter.SavedState>(SavedState()),
    HomeContract.Presenter {

    data class SavedState(val a: Int = 1)

    override fun latestState(): SavedState {
        return SavedState()
    }

    override fun onRestoreState(state: SavedState) {}
}