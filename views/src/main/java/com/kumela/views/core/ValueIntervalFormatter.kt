package com.kumela.views.core

import androidx.annotation.IntRange
import kotlin.math.ceil

class ValueIntervalFormatter {

    fun formatMaxValueIntervals(
        @IntRange(from = 0, to = 100000) max: Int,
        @IntRange(from = 0) size: Int,
    ): List<Int> {
        val nextThousand = (ceil(max / 1000.0) * 1000).toFloat()
        val interval = nextThousand / (size - 1)
        return List(size) { index -> (index * interval).toInt() }
    }
}