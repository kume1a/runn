package com.kumela.mvx.mvp

import com.kumela.mvx.mvp.core.MvpView

abstract class SavedStatePresenter<V : MvpView, S>(initialState: S) :
    MvpBasePresenter<V>() {

    private var state: S = initialState

    abstract fun latestState(): S
    abstract fun onRestoreState(state: S)

    override fun onViewBound() {
        super.onViewBound()
        onRestoreState(state)
    }

    override fun onViewDetached() {
        super.onViewDetached()
        state = latestState()
    }
}