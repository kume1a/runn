package com.kumela.runn.ui.run

import android.content.pm.PackageManager
import com.kumela.mvx.mvp.SavedStatePresenter
import com.kumela.runn.core.events.LocationEvent
import com.kumela.runn.core.events.RunSessionInfoEvent
import com.kumela.runn.core.events.RunSessionTick
import com.kumela.runn.core.roundToSingleDecimal
import com.kumela.runn.data.db.user.UserService
import com.kumela.runn.helpers.calculators.BurnedCalorieCalculator
import com.kumela.runn.ui.core.navigation.ScreenNavigator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class RunPresenter(
    private val runLocationServiceController: RunLocationServiceController,
    private val calorieCalculator: BurnedCalorieCalculator,
    private val screenNavigator: ScreenNavigator,
    private val userService: UserService,
) : SavedStatePresenter<RunContract.View, RunPresenter.State>(State()), RunContract.Presenter {

    data class State(
        val textTime: String = "00:00:00",
        val textKm: String = "0.0",
        val textPace: String = "0.0",
        val textCalories: String = "0.0",
    )

    private var ongoing = false

    override fun onViewAttached() {
        super.onViewAttached()
        EventBus.getDefault().register(this)
    }

    override fun onViewDetached() {
        super.onViewDetached()
        EventBus.getDefault().unregister(this)
    }

    override fun onViewBound() {
        super<SavedStatePresenter>.onViewBound()
        ifViewAttached { view ->
            if (view.isLocationPermissionGranted()) {
                view.showLocationPrompt()
            } else {
                if (view.shouldShowLocationPermissionRationale()) {
                    view.showPermissionRationale(RC_PERMISSION_LOCATION)
                } else {
                    view.requestLocationPermissions(RC_PERMISSION_LOCATION)
                }
            }
        }
    }

    override fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionResult(requestCode, permissions, grantResults)
        if (requestCode == RC_PERMISSION_LOCATION) {
            if (grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED) {
                if (view?.isLocationEnabled() == false) {
                    view?.showLocationPrompt()
                }
            } else {
                view?.showPermissionSettingsRouteView()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationEvent(event: LocationEvent) {
        ifViewAttached { view ->
            if (!ongoing) {
                ongoing = true
                view.changeStartToPause()
            }

            view.drawLine(event.lastLocation, event.currentLocation)
            view.moveCameraTo(event.currentLocation, event.bearing)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRunSessionInfoEvent(event: RunSessionInfoEvent) {
        disposables.add(
            userService.getUser()
                .observeOn(AndroidSchedulers.mainThread())
                .map { user ->
                    calorieCalculator.calculateCaloriesBurned(
                        user.weight.toFloat(),
                        event.averageSpeed.toFloat(),
                        event.fullDurationInMillis * 0.000016667f) // convert millis into minutes ( * 1/60_000 )
                }
                .subscribe { calories ->
                    ifViewAttached { view ->
                        view.setCalories(calories.roundToSingleDecimal().toString())
                        view.setPace(event.speedInKmH.roundToSingleDecimal().toString())
                        view.setDistance(event.fullDistanceInKm.roundToSingleDecimal().toString())
                    }
                }
        )
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRunSessionTick(event: RunSessionTick) {
        view?.setTime(event.duration.format())
    }

    override fun onBackClicked() {

    }

    override fun onStartPauseClicked() {
        ifViewAttached { view ->
            if (!ongoing) {
                if (!view.isLocationEnabled()) {
                    view.showLocationPrompt()
                }

                runLocationServiceController.requestLocationUpdates()
                view.changeStartToPause()
            } else {
                runLocationServiceController.stopLocationUpdates()
                view.showResumeStop()
                view.hideBottomButtons()
            }
            ongoing = !ongoing
        }
    }

    override fun onResumeClicked() {
        ongoing = true
        ifViewAttached { view ->
            runLocationServiceController.requestLocationUpdates()
            view.showBottomButtons()
            view.hideResumeStop()
        }
    }

    override fun onStopClicked() {
        runLocationServiceController.stopLocationUpdates()
        runLocationServiceController.stopService()
        screenNavigator.pop()
    }

    override fun onLockClicked() {
    }

    override fun latestState(): State {
        if (view == null) return State()

        return State(
            textTime = view!!.getTextTime(),
            textKm = view!!.getTextDistance(),
            textPace = view!!.getTextPace(),
            textCalories = view!!.getTextCalories()
        )
    }

    override fun onRestoreState(state: State) {
        ifViewAttached { view ->
            val hasStarted = state != State()
            if (hasStarted) {
                view.changeStartToPause()

                if (!ongoing) {
                    view.showResumeStop()
                    view.hideBottomButtons()
                }
            }

            view.setTime(state.textTime)
            view.setDistance(state.textKm)
            view.setPace(state.textPace)
            view.setCalories(state.textCalories)
        }
    }

    companion object {
        private const val RC_PERMISSION_LOCATION = 1
    }
}