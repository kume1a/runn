package com.kumela.views.model

internal sealed class IndicatorShape {
    data class Circle(val cx: Float, val cy: Float, val radius: Float) : IndicatorShape()
    data class RectF(val left: Float, val top: Float, val right: Float, val bottom: Float) : IndicatorShape()
}