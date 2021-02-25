package com.kumela.runn.ui.core.mvp

import com.kumela.runn.ui.core.mvp.core.MvpView

abstract class SavedStatePresenter<V : MvpView, S>(initialState: S) : MvpBasePresenter<V>() {

    private var state: S = initialState

    abstract fun latestState(): S
    abstract fun onRestoreState(state: S)

    override fun onViewBound() {
        super.onViewBound()
        onRestoreState(state)
    }

    override fun onViewDetaching() {
        super.onViewDetaching()
        state = latestState()
    }
}