package com.kumela.runn.ui.home

import com.kumela.mvx.mvp.MvpBasePresenter
import com.kumela.runn.data.db.run.RunSession
import com.kumela.runn.data.db.run.RunSessionService
import com.kumela.runn.helpers.time.Duration
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.*

class HomePresenter(
    private val runSessionService: RunSessionService,
) : MvpBasePresenter<HomeContract.View>(), HomeContract.Presenter {

    override fun onViewBound() {
        disposables.add(runSessionService.getAllRunSessions(8)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { runSessions ->
                ifViewAttached { view ->
                    view.bindRunSessions(runSessions)
                }
            }
        )

        val targetDistance = 10
        val targetCalories = 2000
        disposables.add(runSessionService.getRunSessionsAfter(getTimestampOfThisWeek())
            .map { runSessions ->
                val totalDistance = runSessions.sumByDouble { it.distanceInKm }
                val totalCalories = runSessions.sumBy { it.caloriesBurned.toInt() }

                val distanceProgress = totalDistance / targetDistance
                val caloriesProgress = totalCalories / targetCalories

                Pair(distanceProgress, caloriesProgress)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { pair ->
                ifViewAttached { view ->
                    Timber.d("onViewBound() called with: view = $view")
                    view.bindDistanceProgress(pair.first.toFloat())
                    view.bindCaloriesProgress(pair.second.toFloat())
                }
            })

        disposables.add(runSessionService.getRunSessionsAfter(getTimestampOfToday())
            .map { runSessions ->
                runSessions.sumByDouble { it.distanceInKm }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { totalDistanceCoveredToday ->
                ifViewAttached { view ->
                    view.bindDistanceCoveredToday(totalDistanceCoveredToday)
                }
            })

        disposables.add(runSessionService.getAllRunSessions()
            .map { runSessions ->
                runSessions.groupBy { runSession ->
                    runSession.timestamp / Duration.millisecondsPerDay
                }.mapValues { entry ->
                    entry.value.sumByDouble { it.distanceInKm } * 1000
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { julianDaysToDistancesInMeters ->
                ifViewAttached { view ->
                    if (julianDaysToDistancesInMeters.isNotEmpty()) {
                        view.bindDistances(julianDaysToDistancesInMeters)
                    }
                }
            })
    }

    override fun onRunSessionClicked(runSession: RunSession) {
        Timber.d("onRunSessionClicked() called with: runSession = $runSession")
    }

    private fun getTimestampOfThisWeek(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        return cal.timeInMillis
    }

    private fun getTimestampOfToday(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        return cal.timeInMillis
    }
}