package com.kumela.runn.ui.core.views.core

import android.util.Size
import android.view.View
import kotlin.math.min

object ViewUtils {

    @JvmStatic
    fun measure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int,
        desiredWidth: Int,
        desiredHeight: Int,
    ): Size {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        //Measure Width
        val width: Int = when (widthMode) {
            View.MeasureSpec.EXACTLY -> widthSize
            View.MeasureSpec.AT_MOST -> min(desiredWidth, widthSize)
            else -> desiredWidth
        }

        //Measure Height
        val height: Int = when (heightMode) {
            View.MeasureSpec.EXACTLY -> heightSize
            View.MeasureSpec.AT_MOST -> min(desiredHeight, heightSize)
            else -> desiredHeight
        }

        return Size(width, height)
    }
}