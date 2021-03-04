package com.kumela.runn.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import butterknife.ButterKnife
import butterknife.Unbinder
import com.kumela.mvx.mvp.MvpBaseController
import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView
import com.kumela.runn.di.injectors.Injector

abstract class BaseController<V: MvpView, P: MvpPresenter<V>> : MvpBaseController<V, P> {

    private var injected = false
    private var unbinder: Unbinder? = null

    constructor()
    constructor(bundle: Bundle): super(bundle)

    override fun onContextAvailable(context: Context) {
        if (!injected) {
            Injector.inject(this)
            injected = true
        }
        super.onContextAvailable(context)
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        val view =  inflater.inflate(layoutRes, container, false)
        unbinder = ButterKnife.bind(this, view)
        onViewBound(view)
        presenter.onViewBound()
        return view
    }

    @get:LayoutRes
    abstract val layoutRes: Int

    protected open fun onViewBound(view: View) {}

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        if (unbinder != null) {
            unbinder!!.unbind()
            unbinder = null
        }
    }

    fun getString(@StringRes stringRes: Int): String? = view?.context?.getString(stringRes)
}