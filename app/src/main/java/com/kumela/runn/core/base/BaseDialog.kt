package com.kumela.runn.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import butterknife.ButterKnife
import butterknife.Unbinder
import com.hippo.conductor.dialog.DialogController

abstract class BaseDialog(@StyleRes theme: Int = 0): DialogController(theme) {

    private var unbinder: Unbinder? = null

    @get:LayoutRes
    abstract val layoutRes: Int

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        return onCreateView(inflater, container)
    }

    final override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup): View? {
        val view = inflater.inflate(layoutRes, container, false)
        unbinder = ButterKnife.bind(this, view)
        onViewBound(view)
        return view
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        unbinder?.unbind()
        unbinder = null
    }

    protected open fun onViewBound(view: View) {}
}