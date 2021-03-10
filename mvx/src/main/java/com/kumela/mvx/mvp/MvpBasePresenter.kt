package com.kumela.mvx.mvp

import android.content.Intent
import androidx.annotation.UiThread
import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.lang.ref.WeakReference


abstract class MvpBasePresenter<V : MvpView> : MvpPresenter<V> {

    fun interface ViewAction<V> {
        fun run(view: V)
    }

    private var viewRef: WeakReference<V>? = null
    protected val disposables = CompositeDisposable()

    protected val view: V?
        @UiThread get() = viewRef?.get()

    @Suppress("unused")
    @UiThread
    protected fun ifViewAttached(
        exceptionIfViewNotAttached: Boolean = false,
        action: ViewAction<V>,
    ) {
        val view = if (viewRef == null) null else viewRef!!.get()
        if (view != null) {
            action.run(view)
        } else check(!exceptionIfViewNotAttached) {
            "No View attached to Presenter."
        }
    }

    final override fun attachView(view: V) {
        viewRef = WeakReference(view)
        onViewAttached()
    }

    final override fun detachView() {
        onViewDetached()
        if (viewRef != null) {
            viewRef!!.clear()
            viewRef = null
        }
        disposables.clear()
    }

    override fun onViewAttached() {}
    override fun onViewDetached() {}

    override fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {}
    override fun onResult(requestCode: Int, resultCode: Int, data: Intent?) {}
}