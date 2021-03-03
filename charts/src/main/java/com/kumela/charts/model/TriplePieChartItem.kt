package com.kumela.charts.model

import android.graphics.RectF

internal data class TriplePieChartItem(
    var startAngle: Float,
    var sweepAngle: Float,
    val rect: RectF,
    val radius: Float
)