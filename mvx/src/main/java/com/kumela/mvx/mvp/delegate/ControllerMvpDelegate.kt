package com.kumela.mvx.mvp.delegate

import android.content.Context
import android.view.View
import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView

internal interface ControllerMvpDelegate<V : MvpView, P : MvpPresenter<V>> {

    fun onContextAvailable(context: Context)
    fun onDestroyView(view: View)
}