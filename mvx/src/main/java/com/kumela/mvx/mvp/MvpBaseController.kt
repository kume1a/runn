package com.kumela.mvx.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bluelinelabs.conductor.Controller
import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView
import com.kumela.mvx.mvp.delegate.ControllerMvpDelegate
import com.kumela.mvx.mvp.delegate.ControllerMvpDelegateImpl
import javax.inject.Inject

@Suppress("unused")
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

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        mvpDelegate.onDestroyView(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.onRequestPermissionResult(requestCode, permissions, grantResults)
    }
}