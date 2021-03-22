package com.kumela.runn.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.kumela.runn.R
import com.kumela.runn.core.base.BaseController
import com.kumela.runn.core.roundToSingleDecimal
import com.kumela.runn.data.db.run.RunSession
import com.kumela.runn.helpers.time.Duration
import com.kumela.runn.ui.home.run_sessions_list.RunSessionsAdapter
import com.kumela.views.TriplePieChart
import com.kumela.views.enums.Ring
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("NonConstantResourceId")
class HomeController : BaseController<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {

    override val layoutRes: Int
        get() = R.layout.screen_home

    @BindView(R.id.button_water_drop) lateinit var buttonWaterDrop: ImageButton
    @BindView(R.id.chart_target) lateinit var chartTarget: TriplePieChart
    @BindView(R.id.text_distance_percentage) lateinit var textDistancePercentage: TextView
    @BindView(R.id.text_calories_percentage) lateinit var textCaloriesPercentage: TextView
    @BindView(R.id.text_water_percentage) lateinit var textWaterPercentage: TextView
    @BindView(R.id.text_distance_amount) lateinit var textDistanceAmount: TextView
    @BindView(R.id.chart_distances) lateinit var chartSteps: BarChart
    @BindView(R.id.recycler_run_sessions) lateinit var recyclerRunSessions: RecyclerView

    private val adapterRunSession = RunSessionsAdapter { runSession -> presenter.onRunSessionClicked(runSession) }

    override fun onViewBound(view: View, savedViewState: Bundle?) {
        recyclerRunSessions.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterRunSession
        }

        chartSteps.apply {
            setDrawGridBackground(false)
            setNoDataText("")
            setPinchZoom(false)
            setFitBars(true)
            setVisibleXRange(1f, 4f)
            isDoubleTapToZoomEnabled = false
            description.isEnabled = false
            legend.isEnabled = false

            axisRight.setDrawLabels(false)
            axisRight.setDrawGridLines(false)
            axisRight.setDrawAxisLine(false)

            axisLeft.textSize = 16f
            axisLeft.typeface = ResourcesCompat.getFont(view.context, R.font.montserrat)
            axisLeft.textColor = ContextCompat.getColor(view.context, R.color.text_primary)
            axisLeft.setDrawGridLines(false)

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.labelCount = 4
            xAxis.textSize = 12f
            xAxis.textColor = ContextCompat.getColor(view.context, R.color.text_primary)
        }
    }

    override fun bindDistanceProgress(progress: Float) {
        val value = normalizeProgress(progress)

        textDistancePercentage.text = getString(R.string.value_percent, value * 100f)
        chartTarget.setProgress(Ring.OUTER, value)
    }

    override fun bindCaloriesProgress(progress: Float) {
        val value = normalizeProgress(progress)

        textCaloriesPercentage.text = getString(R.string.value_percent, value * 100f)
        chartTarget.setProgress(Ring.MIDDLE, value)
    }

    override fun bindWaterProgress(progress: Float) {
        val value = normalizeProgress(progress)

        textWaterPercentage.text = getString(R.string.value_percent, value * 100f)
        chartTarget.setProgress(Ring.INNER, value)
    }

    override fun bindDistanceCoveredToday(distance: Double) {
        textDistanceAmount.text = distance.roundToSingleDecimal().toString()
    }

    override fun bindDistances(julianDaysToDistancesInMeters: Map<Long, Double>) {
        val minDay = julianDaysToDistancesInMeters.minByOrNull { it.key }!!.key
        chartSteps.xAxis.valueFormatter = StepsDateFormatter(minDay)
        val entries = julianDaysToDistancesInMeters.map { entry ->
            val x = entry.key - minDay
            BarEntry(x.toFloat(), entry.value.toFloat())
        }

        val barDataSet = BarDataSet(entries, "").apply {
            color = getColor(R.color.accent)
            setDrawValues(false)
        }

        val data = BarData(barDataSet).apply {
            barWidth = 0.08f * xMax
            setValueTextColor(Color.WHITE)
        }

        chartSteps.apply {
            this.data = data
            moveViewToX(data.xMax)
            animateY(500)
        }
    }

    override fun bindRunSessions(runSessions: List<RunSession>) {
        adapterRunSession.submitList(runSessions)
    }

    private fun normalizeProgress(progress: Float): Float {
        return when {
            progress > 1f -> 1f
            progress <= .001f -> .001f
            else -> progress
        }
    }

    private class StepsDateFormatter(private val minTimestamp: Long) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val timestamp = (value.toLong() + minTimestamp) * Duration.millisecondsPerDay
            val sdf = SimpleDateFormat("MMM d", Locale.getDefault())
            return sdf.format(timestamp)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): HomeController {
            return HomeController()
        }
    }
}