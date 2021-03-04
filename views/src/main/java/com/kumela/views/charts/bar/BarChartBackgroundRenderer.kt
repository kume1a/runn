package com.kumela.views.charts.bar

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.Dimension
import com.kumela.views.core.ValueIntervalFormatter
import com.kumela.views.core.ViewUtils.textBounds

class BarChartBackgroundRenderer {

    private val intervals = mutableListOf<Int>()
    private val valueIntervalFormatter = ValueIntervalFormatter()
    private val intervalPositions = mutableListOf<Float>()

    val interval: Int?
        get() = intervals.getOrNull(1)

    val intervalHeight: Float?
        get() = if (intervalPositions.size >= 2) intervalPositions[0] - intervalPositions[1] else null


    fun updateValues(values: List<Int>, @Dimension textMargin: Float, h: Int, paint: Paint) {
        val max = values.maxOrNull()
        if (max != null) {
            val textHeight = paint.textBounds(max.toString()).height()
            val intervalCount = ((h + textMargin) / (textHeight + textMargin)).toInt()

            intervals.clear()
            intervals.addAll(valueIntervalFormatter.formatMaxValueIntervals(max, intervalCount))

            intervalPositions.clear()
            for (index in 0 until intervals.size) {
                val position = h - index * (textMargin + textHeight)
                intervalPositions.add(position)
            }
        }
    }

    fun render(
        @Dimension lineStartPadding: Float,
        canvas: Canvas,
        paintText: Paint,
        paintLine: Paint,
    ) {
        intervals.zip(intervalPositions).forEach { pair ->
            val interval = pair.first
            val position = pair.second

            renderLabel(interval.toString(), position, canvas, paintText)
            renderBackgroundLine(lineStartPadding, position, canvas, paintLine)
        }
    }

    private fun renderBackgroundLine(
        lineStartPadding: Float,
        position: Float,
        canvas: Canvas,
        paintLine: Paint,
    ) {
        canvas.drawLine(lineStartPadding,
            position,
            canvas.width.toFloat(),
            position,
            paintLine)
    }

    private fun renderLabel(text: String, position: Float, canvas: Canvas, paintText: Paint) {
        canvas.drawText(text, 0f, position, paintText)
    }
}