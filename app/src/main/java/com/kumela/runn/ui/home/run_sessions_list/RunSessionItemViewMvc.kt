package com.kumela.runn.ui.home.run_sessions_list

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kumela.mvx.mvc.BaseObservableViewMvc
import com.kumela.runn.R
import com.kumela.runn.core.roundToSingleDecimal
import com.kumela.runn.data.db.run.RunSession
import com.kumela.runn.helpers.time.Duration

class RunSessionItemViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?,
) : BaseObservableViewMvc<RunSessionItemViewMvc.Listener>(inflater, parent, R.layout.item_run_session) {

    interface Listener {
        fun onRunSessionClicked(runSession: RunSession)
    }

    private var model: RunSession? = null

    private val textPlanName: TextView = findViewById(R.id.text_plan_name)
    private val textRunDate: TextView = findViewById(R.id.text_run_date)
    private val textDistanceInKm: TextView = findViewById(R.id.text_distance_amount)
    private val textSpeedAmount: TextView = findViewById(R.id.text_speed_amount)
    private val textCalories: TextView = findViewById(R.id.text_calories)
    private val textDistanceInMeters: TextView = findViewById(R.id.text_distance_in_metres)
    private val textTimeAmount: TextView = findViewById(R.id.text_time_amount)
    private val imageSnapshot: ImageView = findViewById(R.id.image_snapshot)

    init {
        rootView.setOnClickListener { listener?.onRunSessionClicked(model!!) }
    }

    fun bindRunSession(runSession: RunSession) {
        model = runSession

        // TODO: 21/03/21 add plan name
        textPlanName.text = "Plan"
        textRunDate.text = DateUtils.getRelativeTimeSpanString(context, runSession.timestamp)
        textDistanceInKm.text = runSession.distanceInKm.roundToSingleDecimal().toString()
        textSpeedAmount.text = runSession.averageSpeed.roundToSingleDecimal().toString()
        textCalories.text = getString(R.string.value_cal, runSession.caloriesBurned)
        textDistanceInMeters.text = getString(R.string.value_m, (runSession.distanceInKm * 1000f).toFloat())
        textTimeAmount.text = Duration(milliseconds = runSession.durationInMillis).toSMH()
        imageSnapshot.setImageBitmap(runSession.mapSnapshot)
    }
}