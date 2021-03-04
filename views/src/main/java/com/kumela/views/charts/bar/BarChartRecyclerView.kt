package com.kumela.views.charts.bar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kumela.views.R

internal class BarChartRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0,
) : RecyclerView(context, attrs, defStyleAttrs) {

    var activeColor = ContextCompat.getColor(context, R.color.bar_chart_default_active_bar_color)
    var inactiveColor = ContextCompat.getColor(context, R.color.bar_chart_default_inactive_bar_color)
    var barItemClickListener: ((BarChartItem) -> Unit)? = null

    init {
        val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        lm.stackFromEnd = true
        layoutManager = lm
        adapter = BarAdapter(activeColor, inactiveColor) { barItemClickListener?.invoke(it) }
        overScrollMode = OVER_SCROLL_NEVER
    }

    fun notifyChange() {
        adapter?.notifyDataSetChanged()
    }

    fun bindData(data: List<BarChartItem>?) {
        (adapter as BarAdapter).bindData(data)
    }

    fun updateIntervalHeight(interval: Int, intervalHeight: Float) {
        val adapter = adapter as BarAdapter

        adapter.interval = interval
        adapter.intervalHeight = intervalHeight
    }

    private class BarAdapter(
        @ColorInt private val defaultActiveColor: Int,
        @ColorInt private val defaultInactiveColor: Int,
        private val onBarClickedListener: (BarChartItem) -> Unit,
    ) : RecyclerView.Adapter<BarAdapter.BarViewHolder>(), BarItemViewMvc.Listener {

        private val items = mutableListOf<BarChartItem>()
        private var maxBound = 0

        var interval: Int = 0
        var intervalHeight: Float = 0f

        fun bindData(data: List<BarChartItem>?) {
            items.clear()
            if (data != null) {
                maxBound = data.maxByOrNull { it.value }!!.value

                val diffResult = DiffUtil.calculateDiff(BarChartEntryDiffCallback(items, data))
                items.addAll(data)
                diffResult.dispatchUpdatesTo(this)
            } else {
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarViewHolder {
            val viewMvc = BarItemViewMvc(LayoutInflater.from(parent.context), parent)
            viewMvc.registerListener(this)
            return BarViewHolder(viewMvc)
        }

        override fun onBindViewHolder(holder: BarViewHolder, position: Int) {
            holder.barItemViewMvc.bind(items[position], defaultActiveColor, defaultInactiveColor, interval, intervalHeight)
        }

        override fun getItemCount(): Int = items.size

        private class BarViewHolder(val barItemViewMvc: BarItemViewMvc) : RecyclerView.ViewHolder(barItemViewMvc.rootView)

        override fun onBarClicked(barChartItem: BarChartItem) {
            val position = items.indexOf(barChartItem)

            items[position].isSelected = !items[position].isSelected
            var selectedPosition: Int? = null
            if (items[position].isSelected) {
                items.forEachIndexed { index, item ->
                    if (index != position && item.isSelected) {
                        item.isSelected = false
                        selectedPosition = index
                    }
                }
            }
            notifyItemChanged(position)
            if (selectedPosition != null) notifyItemChanged(selectedPosition!!)

            if (items[position].isSelected) {
                onBarClickedListener.invoke(barChartItem)
            }
        }

        private class BarChartEntryDiffCallback(
            private val oldList: List<BarChartItem>,
            private val newList: List<BarChartItem>,
        ) : DiffUtil.Callback() {

            override fun getOldListSize(): Int = oldList.size

            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition].id == newList[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}