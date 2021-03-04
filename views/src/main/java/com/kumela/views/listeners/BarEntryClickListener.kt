package com.kumela.views.listeners

import com.kumela.views.model.BarChartEntry

fun interface BarEntryClickListener {
    fun onBarClicked(barChartEntry: BarChartEntry)
}