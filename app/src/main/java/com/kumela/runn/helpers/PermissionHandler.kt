package com.kumela.runn.helpers

import android.content.Context
import com.bluelinelabs.conductor.Controller

interface PermissionHandler {
    fun isLocationPermissionGranted(context: Context): Boolean
    fun shouldShowLocationPermissionRationale(controller: Controller): Boolean
    fun requestLocationPermission(controller: Controller, requestCode: Int)
}