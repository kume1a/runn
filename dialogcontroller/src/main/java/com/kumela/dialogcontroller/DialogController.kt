package com.kumela.dialogcontroller

import android.content.DialogInterface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import com.bluelinelabs.conductor.Controller

abstract class DialogController @JvmOverloads constructor(@StyleRes theme: Int = 0) : Controller(), DialogInterface {

    init {
        args.putInt(KEY_THEME_ID, theme)
    }

    private var _themeId = 0
    var themeId: Int = 0
        @StyleRes get() {
            if (_themeId != 0) {
                return _themeId
            }

            if (activity == null) return 0

            val tv = TypedValue()
            activity!!.theme.resolveAttribute(android.R.attr.dialogTheme, tv, true)
            _themeId = tv.resourceId
            return tv.resourceId
        }
        private set

    var cancellable: Boolean = true
    var cancelledOnTouchOutside: Boolean = true
        set(value) {
            if (value && !cancellable) {
                cancellable = true
            }

            if (cancelledOnTouchOutside != value) {
                field = value
                view?.let { v ->
                    val root = v.findViewById<DialogRootView>(R.id.dialog_root)
                    root?.cancellableOnTouchOutside = value
                }
            }
        }

    private var cancelled: Boolean = false
    private var dismissed: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        val layoutInflater = resolveLayoutInflater(inflater)
        val view = layoutInflater.inflate(R.layout.dialog, container, false)

        val root = view.findViewById<DialogRootView>(R.id.dialog_root)
        root.dialog = this
        root.cancellableOnTouchOutside = cancelledOnTouchOutside

        val content = view.findViewById<DialogContentView>(R.id.dialog_content)
        val dialogContent = createContentView(layoutInflater, content)
        content.addView(dialogContent)

        return view
    }

    abstract fun createContentView(inflater: LayoutInflater, container: ViewGroup): View

    override fun cancel() {
        if (!cancelled && !dismissed) {
            cancelled = true
            onCancel()
            dismiss()
        }
    }

    override fun dismiss() {
        if (!dismissed) {
            dismissed = true
            onDismiss()
            val router = router
            if (router != null) {
                if (!router.popController(this)) {
                    val activity = activity
                    activity?.finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!dismissed) {
            dismissed = true
            onDismiss()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        cancellable = savedInstanceState.getBoolean(KEY_CANCELLABLE, cancellable)
        cancelledOnTouchOutside = savedInstanceState.getBoolean(KEY_CANCELLED_ON_TOUCH_OUTSIZE, cancelledOnTouchOutside)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_CANCELLABLE, cancellable)
        outState.putBoolean(KEY_CANCELLED_ON_TOUCH_OUTSIZE, cancelledOnTouchOutside)
    }

    private fun resolveLayoutInflater(inflater: LayoutInflater): LayoutInflater {
        if (themeId != 0) {
            val contextThemeWrapper = ContextThemeWrapper(inflater.context, themeId)
            return inflater.cloneInContext(contextThemeWrapper)
        }
        return inflater
    }

    fun onDismiss() {}
    fun onCancel() {}

    companion object {
        private const val KEY_THEME_ID = "${BuildConfig.LIBRARY_PACKAGE_NAME}.theme_id"
        private const val KEY_CANCELLABLE = "${BuildConfig.LIBRARY_PACKAGE_NAME}.key_cancellable"
        private const val KEY_CANCELLED_ON_TOUCH_OUTSIZE = "${BuildConfig.LIBRARY_PACKAGE_NAME}.cancelled_on_touch_outsize"
    }
}