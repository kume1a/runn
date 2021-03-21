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
import com.kumela.runn.data.db.run.RunSession
import com.kumela.runn.ui.home.run_sessions_list.RunSessionsAdapter
import com.kumela.views.charts.TriplePieChart
import com.kumela.views.charts.bar.BarChart

@SuppressLint("NonConstantResourceId")
class HomeController : BaseController<HomeContract.View, HomeContract.Presenter>(), HomeContract.View {

    override val layoutRes: Int
        get() = R.layout.screen_home

    @BindView(R.id.button_water_drop) lateinit var buttonWaterDrop: ImageButton
    @BindView(R.id.chart_target) lateinit var chartTarget: TriplePieChart
    @BindView(R.id.text_steps_percentage) lateinit var textStepsPercentage: TextView
    @BindView(R.id.text_calories_percentage) lateinit var textCaloriesPercentage: TextView
    @BindView(R.id.text_water_percentage) lateinit var textWaterPercentage: TextView
    @BindView(R.id.text_steps_count) lateinit var textStepsCount: TextView
    @BindView(R.id.chart_steps) lateinit var chartSteps: BarChart
    @BindView(R.id.recycler_run_sessions) lateinit var recyclerRunSessions: RecyclerView

    private val adapterRunSession = RunSessionsAdapter { runSession -> presenter.onRunSessionClicked(runSession) }

    override fun onViewBound(view: View, savedViewState: Bundle?) {
        recyclerRunSessions.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterRunSession
        }
    }

    override fun bindRunSessions(runSessions: List<RunSession>) {
        adapterRunSession.submitList(runSessions)
    }

    companion object {
        @JvmStatic
        fun newInstance(): HomeController {
            return HomeController()
        }
    }
}