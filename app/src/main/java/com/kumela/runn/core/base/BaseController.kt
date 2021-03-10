package com.kumela.runn.core.base

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.core.content.ContextCompat
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.kumela.mvx.mvp.MvpBaseController
import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView
import com.kumela.runn.core.lifecycle.ScreenLifecycleTask
import com.kumela.runn.di.injectors.Injector
import javax.inject.Inject

abstract class BaseController<V : MvpView, P : MvpPresenter<V>> : MvpBaseController<V, P> {

    private var injected = false
    private var unbinder: Unbinder? = null

    constructor()
    constructor(bundle: Bundle) : super(bundle)

    @Inject lateinit var lifecycleTasks: Set<@JvmSuppressWildcards ScreenLifecycleTask>

    override fun onContextAvailable(context: Context) {
        if (!injected) {
            Injector.inject(this)
            injected = true
        }
        for (lifecycleTask in lifecycleTasks) {
            lifecycleTask.onContextAvailable(context, activity)
        }
        super.onContextAvailable(context)
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?,
    ): View {
        val view = inflater.inflate(layoutRes, container, false)
        unbinder = ButterKnife.bind(this, view)
        onViewBound(view, savedViewState)
        presenter.onViewBound()
        return view
    }

    @get:LayoutRes
    abstract val layoutRes: Int

    protected open fun onViewBound(view: View, savedViewState: Bundle?) {}

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        if (unbinder != null) {
            unbinder!!.unbind()
            unbinder = null
        }
    }

    override fun onChangeStarted(changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
        super.onChangeStarted(changeHandler, changeType)
        for (lifecycleTask in lifecycleTasks) {
            if (changeType.isEnter) {
                lifecycleTask.onEnterScope(view)
            } else {
                lifecycleTask.onExitScope(view)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        for (lifecycleTask in lifecycleTasks) {
            lifecycleTask.onDestroy()
        }
    }

    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        for (lifecycleTask in lifecycleTasks) {
            lifecycleTask.onActivityPaused(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        for (lifecycleTask in lifecycleTasks) {
            lifecycleTask.onActivityResumed(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        super.onActivityStarted(activity)
        for (lifecycleTask in lifecycleTasks) {
            lifecycleTask.onActivityStarted(activity)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        super.onActivityStopped(activity)
        for (lifecycleTask in lifecycleTasks) {
            lifecycleTask.onActivityStopped(activity)
        }
    }

    protected fun getString(@StringRes stringRes: Int): String = view?.context?.getString(stringRes) ?: ""

    @Dimension
    protected fun getDimension(@DimenRes dimenRes: Int): Float = view?.context?.resources?.getDimension(dimenRes) ?: 0f

    protected fun getDrawable(@DrawableRes drawableRes: Int): Drawable? =
        if (view?.context != null) ContextCompat.getDrawable(view!!.context, drawableRes) else null
}