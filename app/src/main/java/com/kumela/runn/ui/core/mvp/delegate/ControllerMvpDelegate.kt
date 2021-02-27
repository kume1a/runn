package com.kumela.runn.ui.core.mvp.delegate

import android.app.Activity
import android.content.Context
import android.view.View
import com.kumela.runn.ui.core.mvp.core.MvpPresenter
import com.kumela.runn.ui.core.mvp.core.MvpView

interface ControllerMvpDelegate<V : MvpView, P : MvpPresenter<V>> {

    fun onContextAvailable(context: Context)

    fun onActivityResumed(activity: Activity)
    fun onActivityStarted(activity: Activity)
    fun onActivityPaused(activity: Activity)
    fun onActivityStopped(activity: Activity)

    fun onAttach(view: View)
    fun onDetach(view: View)

    fun onDestroyView(view: View)
    fun onDestroy()
}