package com.kumela.runn.ui.home

import android.view.View
import butterknife.BindView
import com.kumela.runn.R
import com.kumela.runn.core.base.BaseController
import com.kumela.views.charts.bar.BarChart
import com.kumela.views.model.BarChartEntry
import java.util.*

class HomeController : BaseController<HomeContract.View, HomeContract.Presenter>() {
    override val layoutRes: Int
        get() = R.layout.screen_home

    @BindView(R.id.chart_steps) lateinit var chartSteps: BarChart

    override fun onViewBound(view: View) {
        super.onViewBound(view)

        chartSteps.bindData(listOf(
            BarChartEntry(2, 4350, Calendar.getInstance().time),
            BarChartEntry(0,100, Calendar.getInstance().time),
            BarChartEntry(2, 1280, Calendar.getInstance().time),
            BarChartEntry(2, 700, Calendar.getInstance().time),
        ))
    }

    companion object {
        @JvmStatic
        fun newInstance(): HomeController {
            return HomeController()
        }
    }
}