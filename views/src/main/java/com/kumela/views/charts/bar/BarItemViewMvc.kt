package com.kumela.views.charts.bar

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kumela.mvx.mvc.BaseObservableViewMvc
import com.kumela.views.R

internal class BarItemViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?,
) : BaseObservableViewMvc<BarItemViewMvc.Listener>(inflater, parent, R.layout.item_bar) {

    fun interface Listener {
        fun onBarClicked(barChartItem: BarChartItem)
    }

    private val viewBar: View = findViewById(R.id.view_bar)
    private val textLabel: TextView = findViewById(R.id.text_label)

    private var barChartItem: BarChartItem? = null

    init {
        rootView.setOnClickListener {
            listener?.onBarClicked(barChartItem!!)
        }
    }

    fun bind(
        barChartItem: BarChartItem,
        activeColor: Int,
        inactiveColor: Int,
        interval: Int,
        intervalHeight: Float,
    ) {
        this.barChartItem = barChartItem

        viewBar.backgroundTintList = ColorStateList.valueOf(if (barChartItem.isSelected) activeColor else inactiveColor)
        textLabel.visibility = if (barChartItem.isSelected) View.VISIBLE else View.GONE
        textLabel.text = barChartItem.value.toString()

        val barLayoutParams = viewBar.layoutParams
        barLayoutParams.height = ((barChartItem.value.toFloat() / interval.toFloat()) * intervalHeight).toInt()
        viewBar.layoutParams = barLayoutParams
    }
}