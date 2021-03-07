package com.kumela.runn.ui.run

import android.content.pm.PackageManager
import com.kumela.mvx.mvp.MvpBasePresenter
import com.kumela.runn.data.managers.RequestingLocationManager


class RunPresenter(
    private val requestingLocationManager: RequestingLocationManager,
) : MvpBasePresenter<RunContract.View>(), RunContract.Presenter {

    override fun onViewBound() {
        super<MvpBasePresenter>.onViewBound()
        if (requestingLocationManager.requestingLocationUpdates() && view?.isLocationPermissionGranted() == false) {
            view?.requestLocationPermissions(RC_PERMISSION_LOCATION)
        }
    }

    override fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionResult(requestCode, permissions, grantResults)
        if (requestCode == RC_PERMISSION_LOCATION) {
            if (grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED) view?.startService()
            else view?.showPermissionSettingsRouteView()
        }
    }

    override fun onStartClicked() {
        ifViewAttached { view ->
            if (view.isLocationPermissionGranted()) {
                view.startService()
            } else {
                if (view.shouldShowLocationPermissionRationale()) {
                    view.showPermissionRationale(RC_PERMISSION_LOCATION)
                } else {
                    view.requestLocationPermissions(RC_PERMISSION_LOCATION)
                }
            }
        }
    }

    override fun onStopClicked() {
        view?.endService()
    }

    companion object {
        private const val RC_PERMISSION_LOCATION = 1
    }
}