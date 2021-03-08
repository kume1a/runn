package com.kumela.runn.ui.run

import android.content.pm.PackageManager
import com.kumela.mvx.mvp.MvpBasePresenter


class RunPresenter(
    private val runLocationServiceController: RunLocationServiceController,
) : MvpBasePresenter<RunContract.View>(), RunContract.Presenter {

    override fun onViewBound() {
        super<MvpBasePresenter>.onViewBound()
        if (view?.isLocationPermissionGranted() == false) {
            view?.requestLocationPermissions(RC_PERMISSION_LOCATION)
        } else {
            view?.showLocationPrompt()
        }
    }

    override fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionResult(requestCode, permissions, grantResults)
        if (requestCode == RC_PERMISSION_LOCATION) {
            if (grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED) {
                runLocationServiceController.startService()
                view?.showLocationPrompt()
            } else view?.showPermissionSettingsRouteView()
        }
    }

    override fun onStartClicked() {
        ifViewAttached { view ->
            if (view.isLocationPermissionGranted()) {
                runLocationServiceController.startService()
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
        runLocationServiceController.endService()
    }

    companion object {
        private const val RC_PERMISSION_LOCATION = 1
    }
}