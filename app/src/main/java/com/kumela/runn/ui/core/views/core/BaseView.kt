package com.kumela.runn.ui.core.views.core

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import kotlin.math.min
import kotlin.math.roundToInt

abstract class BaseView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    /**
     * default width of view in dps
     */
    protected abstract val defaultWidth: Int

    /**
     * default height of view in dps
     */
    protected abstract val defaultHeight: Int

    final override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredWidth: Int = defaultWidth.toFloat().dpToPx().toInt()
        val desiredHeight: Int = defaultHeight.toFloat().dpToPx().toInt()

        //Measure Width
        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize)
            else -> desiredWidth
        }

        //Measure Height
        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desiredHeight, heightSize)
            else -> desiredHeight
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) return
    }


    protected fun Float.dpToPx(): Float =
        (this * resources.displayMetrics.density).roundToInt().toFloat()

    protected fun Float.spToPx(): Float =
        (this * resources.displayMetrics.scaledDensity).roundToInt().toFloat()
}
