package com.kumela.runn.ui.run

import com.google.android.gms.maps.model.LatLng
import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView


interface RunContract {

    interface View: MvpView {
        fun isLocationPermissionGranted(): Boolean
        fun shouldShowLocationPermissionRationale(): Boolean
        fun requestLocationPermissions(requestCode: Int)
        fun showPermissionRationale(requestCode: Int)
        fun showPermissionSettingsRouteView()

        fun isLocationEnabled(): Boolean
        fun showLocationPrompt()

        fun setTime(time: String)
        fun getTextTime(): String
        fun setDistance(km: String)
        fun getTextDistance(): String
        fun setPace(pace: String)
        fun getTextPace(): String
        fun setCalories(calories: String)
        fun getTextCalories(): String

        fun changeStartToPause()

        fun showResumeStop()
        fun hideResumeStop()
        fun showBottomButtons()
        fun hideBottomButtons()

        fun drawLine(a: LatLng, b: LatLng)
        fun moveCameraTo(position: LatLng, bearing: Float)
    }

    interface Presenter: MvpPresenter<View> {
        fun onBackClicked()
        fun onStartPauseClicked()
        fun onResumeClicked()
        fun onStopClicked()
        fun onLockClicked()
    }
}