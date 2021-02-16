package com.kumela.runn.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.kumela.runn.di.injectors.Injector
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseController : Controller {

    private val compositeDisposable = CompositeDisposable()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        val view =  inflater.inflate(layoutRes, container, false)
        unbinder = ButterKnife.bind(this, view)
        onViewBound(view)
        compositeDisposable.addAll(*disposables())
        return view
    }

    protected fun onViewBound(view: View) {}

    protected fun disposables(): Array<Disposable> = emptyArray()

    @get:LayoutRes
    protected abstract val layoutRes: Int

    override fun onChangeStarted(
        changeHandler: ControllerChangeHandler,
        changeType: ControllerChangeType
    ) {
        super.onChangeStarted(changeHandler, changeType)
//        for (task in screenLifecycleTasks) {
//            if (changeType.isEnter) {
//                task.onEnterScope(view)
//            } else {
//                task.onExitScope(view)
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        for (task in screenLifecycleTasks) {
//            task.onDestroy(view)
//        }
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        compositeDisposable.clear()
        if (unbinder != null) {
            unbinder!!.unbind()
            unbinder = null
        }
    }
}