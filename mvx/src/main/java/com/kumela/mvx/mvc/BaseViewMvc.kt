package com.kumela.mvx.mvc

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.core.content.ContextCompat

abstract class BaseViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    @LayoutRes private val layoutId: Int
) : ViewMvc {

    override val rootView: View = inflater.inflate(layoutId, parent, false)

    protected val context: Context get() = rootView.context

    protected fun <T : View?> findViewById(@IdRes id: Int): T = rootView.findViewById<T>(id)

    protected fun getString(@StringRes id: Int): String = context.getString(id)
    protected fun getString(@StringRes id: Int, vararg formatArgs: Any): String = context.getString(id, *formatArgs)

    protected fun getStringArray(@ArrayRes id: Int): Array<String> = context.resources.getStringArray(id)

    protected fun getColor(@ColorRes id: Int): Int = ContextCompat.getColor(context, id)

    protected fun getDrawable(@DrawableRes id: Int): Drawable? = ContextCompat.getDrawable(context, id)
}