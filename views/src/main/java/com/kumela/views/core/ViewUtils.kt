package com.kumela.views.core

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.view.animation.AccelerateDecelerateInterpolator
import kotlin.math.roundToInt

internal object ViewUtils {

    val accelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()

    fun dpToPx(context: Context, dp: Float): Float =
        (dp * context.resources.displayMetrics.density).roundToInt().toFloat()

    fun spToPx(context: Context, sp: Float): Float =
        (sp * context.resources.displayMetrics.scaledDensity).roundToInt().toFloat()

    fun Paint.textBounds(text: String) : Rect {
        val textBounds = Rect()
        getTextBounds(text, 0, text.length, textBounds)
        return textBounds
    }
}