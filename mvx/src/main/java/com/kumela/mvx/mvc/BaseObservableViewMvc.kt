package com.kumela.mvx.mvc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread

abstract class BaseObservableViewMvc<Listener>(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    @LayoutRes layoutId: Int
) : BaseViewMvc(inflater, parent, layoutId), ObservableViewMvc<Listener> {

    @Suppress("MemberVisibilityCanBePrivate")
    protected var listener: Listener? = null

    @MainThread
    override fun registerListener(listener: Listener) {
        this.listener = listener
    }

    @MainThread
    override fun unregisterListener() {
        this.listener = null
    }
}