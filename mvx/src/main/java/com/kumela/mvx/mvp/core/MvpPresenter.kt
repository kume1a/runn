package com.kumela.mvx.mvp.core

import androidx.annotation.UiThread

/**
 * The base interface for each mvp presenter
 *
 */
interface MvpPresenter<V: MvpView> {
    /**
     * Set or attach the view to this presenter
     */
    @UiThread
    fun attachView(view: V)

    /**
     * Will be called if the view has been detached from the Presenter.
     * Usually this happens on screen orientation changes or view (like fragment) has been put on the back stack.
     */
    @UiThread
    fun detachView()

    @UiThread
    fun onViewAttached()

    @UiThread
    fun onViewDetaching()

    @UiThread
    fun onViewBound() {}
}