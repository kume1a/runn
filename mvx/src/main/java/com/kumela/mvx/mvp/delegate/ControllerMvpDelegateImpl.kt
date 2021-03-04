package com.kumela.mvx.mvp.delegate

import android.content.Context
import android.view.View
import com.kumela.mvx.mvp.MvpBaseController
import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView


internal class ControllerMvpDelegateImpl<V : MvpView, P : MvpPresenter<V>>(
    private var delegateCallback: MvpBaseController<V, P>
//    private var controller: Controller
) : ControllerMvpDelegate<V, P> {

    private var viewAttached = false

    private val presenter: P
        get() = delegateCallback.presenter

    private val mvpView: V
        get() = delegateCallback.mvpView

    override fun onContextAvailable(context: Context) {
        if (!viewAttached) {
            presenter.attachView(mvpView)

            viewAttached = true
        }
    }

    override fun onDestroyView(view: View) {
        presenter.detachView()
    }
}