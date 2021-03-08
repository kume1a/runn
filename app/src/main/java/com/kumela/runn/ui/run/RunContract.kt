package com.kumela.runn.ui.run

import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView


interface RunContract {

    interface View: MvpView {
        fun isLocationPermissionGranted(): Boolean
        fun shouldShowLocationPermissionRationale(): Boolean
        fun requestLocationPermissions(requestCode: Int)
        fun showPermissionRationale(requestCode: Int)
        fun showPermissionSettingsRouteView()
        fun showLocationPrompt()
    }

    interface Presenter: MvpPresenter<View> {
        fun onStartClicked()
        fun onStopClicked()
    }

}