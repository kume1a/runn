package com.kumela.runn.ui.core.mvp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import com.bluelinelabs.conductor.Controller
import com.kumela.runn.ui.core.mvp.core.MvpPresenter
import com.kumela.runn.ui.core.mvp.core.MvpView
import com.kumela.runn.ui.core.mvp.delegate.ControllerMvpDelegate
import com.kumela.runn.ui.core.mvp.delegate.ControllerMvpDelegateImpl
import javax.inject.Inject

abstract class MvpBaseController<V : MvpView, P : MvpPresenter<V>> : Controller, MvpView {

    constructor()
    constructor(bundle: Bundle) : super(bundle)

    private val mvpDelegate: ControllerMvpDelegate<V, P> by lazy {
        ControllerMvpDelegateImpl(this)
    }

    @Inject lateinit var presenter: P

    @Suppress("UNCHECKED_CAST")
    val mvpView: V
        get() = this as V

    override fun onContextAvailable(context: Context) {
        super.onContextAvailable(context)
        mvpDelegate.onContextAvailable(context)
    }

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        mvpDelegate.onActivityResumed(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        super.onActivityStarted(activity)
        mvpDelegate.onActivityStarted(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        mvpDelegate.onActivityPaused(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        super.onActivityStopped(activity)
        mvpDelegate.onActivityStopped(activity)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        mvpDelegate.onAttach(view)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        mvpDelegate.onDetach(view)
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        mvpDelegate.onDestroyView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        mvpDelegate.onDestroy()
    }
}