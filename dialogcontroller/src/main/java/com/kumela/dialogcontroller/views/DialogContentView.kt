package com.kumela.dialogcontroller.views

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.core.view.ViewCompat

class DialogContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val minWidthMajor = TypedValue()
    private val minWidthMinor = TypedValue()

    init {
        val theme = context.theme
        theme.resolveAttribute(android.R.attr.windowMinWidthMajor, minWidthMajor, true)
        theme.resolveAttribute(android.R.attr.windowMinWidthMinor, minWidthMinor, true)

        val drawable: Drawable = ResourceUtils.getAttrDrawable(context, android.R.attr.windowBackground)
        ViewCompat.setBackground(this, drawable)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val elevation: Float = ResourceUtils.getAttrDimension(context, android.R.attr.windowElevation)
            ViewCompat.setElevation(this, elevation)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        @Suppress("LocalVariableName")
        var _widthMeasureSpec = widthMeasureSpec

        val widthMode = MeasureSpec.getMode(_widthMeasureSpec)
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val tv = if (isPortrait) minWidthMinor else minWidthMajor
        var measure = false

        if (widthMode == MeasureSpec.AT_MOST && tv.type != TypedValue.TYPE_NULL) {
            val min: Int
            val metrics = context.resources.displayMetrics
            min = when (tv.type) {
                TypedValue.TYPE_DIMENSION -> tv.getDimension(metrics).toInt()
                TypedValue.TYPE_FRACTION -> tv.getFraction(metrics.widthPixels.toFloat(), metrics.widthPixels.toFloat()).toInt()
                else -> 0
            }
            if (measuredWidth < min) {
                _widthMeasureSpec = MeasureSpec.makeMeasureSpec(min, MeasureSpec.EXACTLY)
                measure = true
            }
        }

        if (measure) {
            super.onMeasure(_widthMeasureSpec, heightMeasureSpec)
        }
    }
}