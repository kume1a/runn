package com.kumela.runn.ui.home

import androidx.annotation.FloatRange
import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView
import com.kumela.runn.data.db.run.RunSession


interface HomeContract {
    interface View : MvpView {
        fun bindDistanceProgress(@FloatRange(from = 0.0, to = 1.0) progress: Float)
        fun bindCaloriesProgress(@FloatRange(from = 0.0, to = 1.0) progress: Float)
        fun bindWaterProgress(@FloatRange(from = 0.0, to = 1.0) progress: Float)

        fun bindDistanceCoveredToday(distance: Double)
        fun bindDistances(julianDaysToDistancesInMeters: Map<Long, Double>)

        fun bindRunSessions(runSessions: List<RunSession>)
    }

    interface Presenter : MvpPresenter<View> {
        fun onRunSessionClicked(runSession: RunSession)
    }
}