package com.kumela.runn.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.bluelinelabs.conductor.Controller
import com.kumela.runn.di.annotations.ActivityScope
import javax.inject.Inject

@Suppress("SameParameterValue")
@ActivityScope
class DefaultPermissionHandler @Inject constructor(): PermissionHandler {

    private fun permissionGranted(context: Context, permission: String): Boolean =
        ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRationale(controller: Controller, permission: String) =
        controller.shouldShowRequestPermissionRationale(permission)

    private fun requestPermissions(controller: Controller, permissions: Array<String>, requestCode: Int) {
        controller.requestPermissions(permissions, requestCode)
    }

    private fun requestPermission(controller: Controller, permission: String, requestCode: Int) {
        requestPermissions(controller, arrayOf(permission), requestCode)
    }

    override fun isLocationPermissionGranted(context: Context): Boolean =
        permissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)

    override fun shouldShowLocationPermissionRationale(controller: Controller): Boolean =
        shouldShowRationale(controller, Manifest.permission.ACCESS_FINE_LOCATION)

    override fun requestLocationPermission(controller: Controller, requestCode: Int) {
        requestPermission(controller, Manifest.permission.ACCESS_FINE_LOCATION, requestCode)
    }
}