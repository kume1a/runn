package com.kumela.views.charts.bar

import java.util.Date

internal data class BarChartItem(
    val id: Int,
    val value: Int,
    val date: Date,
    var isSelected: Boolean,
)