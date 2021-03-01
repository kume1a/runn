package com.kumela.runn.ui.core.mvp.delegate

import android.content.Context
import android.view.View
import com.kumela.runn.ui.core.mvp.core.MvpPresenter
import com.kumela.runn.ui.core.mvp.core.MvpView

interface ControllerMvpDelegate<V : MvpView, P : MvpPresenter<V>> {

    fun onContextAvailable(context: Context)
    fun onDestroyView(view: View)
}