package com.kumela.views.charts.bar

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    internal data class BarChartItem(
        val id: Int,
        val value: Int,
        var isSelected: Boolean,
    )

    var activeColor = ContextCompat.getColor(context, R.color.bar_chart_default_active_bar_color)
    var inactiveColor = ContextCompat.getColor(context, R.color.bar_chart_default_inactive_bar_color)

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        adapter = BarAdapter(activeColor, inactiveColor)
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
    ) : RecyclerView.Adapter<BarAdapter.BarViewHolder>() {

        private val items = mutableListOf<BarChartItem>()
        private var maxBound = 0

        var interval: Int = 0
        var intervalHeight: Float = 0f

        private fun interface OnBarClickedListener {
            fun onBarClicked(position: Int)
        }

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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bar, parent, false)
            return BarViewHolder(view) { position ->
                items[position].isSelected = !items[position].isSelected
                var selectedPosition: Int? = null
                if (items[position].isSelected) {
                    items.forEachIndexed { index, barChartItem ->
                        if (index != position && barChartItem.isSelected) {
                            barChartItem.isSelected = false
                            selectedPosition = index
                        }
                    }
                }
                notifyItemChanged(position)
                if (selectedPosition != null) notifyItemChanged(selectedPosition!!)
            }
        }

        override fun onBindViewHolder(holder: BarViewHolder, position: Int) {
            val item = items[position]

            holder.viewBar.backgroundTintList = ColorStateList.valueOf(if (item.isSelected) defaultActiveColor else defaultInactiveColor)
            holder.textLabel.visibility = if (item.isSelected) View.VISIBLE else View.GONE
            holder.textLabel.text = item.value.toString()

            val barLayoutParams = holder.viewBar.layoutParams
            barLayoutParams.height = ((item.value.toFloat() / interval.toFloat()) * intervalHeight).toInt()
            holder.viewBar.layoutParams = barLayoutParams
        }

        override fun getItemCount(): Int = items.size

        private class BarViewHolder(itemView: View, onBarClickListener: OnBarClickedListener) : RecyclerView.ViewHolder(itemView) {
            val viewBar: View = itemView.findViewById(R.id.view_bar)
            val textLabel: TextView = itemView.findViewById(R.id.text_label)

            init {
                itemView.setOnClickListener {
                    onBarClickListener.onBarClicked(adapterPosition)
                }
            }
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