package com.kumela.runn.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.kumela.runn.R
import com.kumela.runn.core.base.BaseController
import com.kumela.runn.core.roundToSingleDecimal
import com.kumela.runn.data.db.run.RunSession
import com.kumela.runn.ui.home.run_sessions_list.RunSessionsAdapter
import com.kumela.views.charts.TriplePieChart
import com.kumela.views.charts.bar.BarChart
import com.kumela.views.enums.Ring
import com.kumela.views.model.BarChartEntry
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

    override fun bindDistances(distances: List<Double>) {
        chartSteps.bindData(List(distances.size) { index ->
            BarChartEntry(index, distances[index].toInt(), Calendar.getInstance().time)
        })
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

    companion object {
        @JvmStatic
        fun newInstance(): HomeController {
            return HomeController()
        }
    }
}