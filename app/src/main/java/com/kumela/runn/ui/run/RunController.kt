package com.kumela.runn.ui.run

import android.annotation.SuppressLint
import android.content.*
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ImageView
import butterknife.BindView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.snackbar.Snackbar
import com.kumela.runn.BuildConfig
import com.kumela.runn.R
import com.kumela.runn.core.base.BaseController
import com.kumela.runn.helpers.PermissionHandler
import timber.log.Timber
import javax.inject.Inject


@SuppressLint("NonConstantResourceId")
class RunController : BaseController<RunContract.View, RunContract.Presenter>(), RunContract.View {

    override val layoutRes: Int
        get() = R.layout.screen_run

    @BindView(R.id.button_start) lateinit var buttonStart: Button
    @BindView(R.id.button_stop) lateinit var buttonStop: Button
    @BindView(R.id.map_view) lateinit var mapView: MapView

    private var map: GoogleMap? = null

    @Inject lateinit var permissionHandler: PermissionHandler

    override fun onViewBound(view: View, savedViewState: Bundle?) {
        super.onViewBound(view, savedViewState)

        buttonStart.setOnClickListener { presenter.onStartClicked() }
        buttonStop.setOnClickListener { presenter.onStopClicked() }
        mapView.getMapAsync { googleMap ->
            map = googleMap
            try {
                map!!.isMyLocationEnabled = true
                val buttonMyLocation = mapView.findViewById<ImageView>("2".toInt())
                buttonMyLocation.setImageResource(R.drawable.ic_gps)
            } catch (e: SecurityException) {
                Timber.e(e)
            }

            try {
                val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(view.context, R.raw.map_style))
                if (!success) {
                    Timber.e("parsing json failed")
                }
            } catch (e: Resources.NotFoundException) {
                Timber.e(e, "map_style.json not found")
            }
        }
        mapView.onCreate(savedViewState)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        mapView.onResume()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        mapView.onPause()
    }

    override fun onDestroyView(view: View) {
        mapView.onDestroy()
        super.onDestroyView(view)
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun isLocationPermissionGranted(): Boolean {
        return permissionHandler.isLocationPermissionGranted(activity!!)
    }

    override fun shouldShowLocationPermissionRationale(): Boolean {
        return permissionHandler.shouldShowLocationPermissionRationale(this)
    }

    override fun requestLocationPermissions(requestCode: Int) {
        permissionHandler.requestLocationPermission(this, requestCode)
    }

    override fun showPermissionRationale(requestCode: Int) {
        if (view != null && activity != null) {
            Snackbar.make(view!!, getString(R.string.location_permission_rationale), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.ok)) {
                    permissionHandler.requestLocationPermission(this@RunController, requestCode)
                }
                .show()
        }
    }

    override fun showPermissionSettingsRouteView() {
        if (view != null && activity != null) {
            Snackbar.make(view!!, getString(R.string.location_permission_denied_explanation), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.settings)) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri: Uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                .show()
        }
    }

    override fun showLocationPrompt() {
        if (activity == null) return

        val locationRequest = LocationRequest.create().also { request ->
            request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val result = LocationServices.getSettingsClient(activity!!).checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        try {
                            val resolvable = exception as ResolvableApiException
                            resolvable.startResolutionForResult(activity!!, LocationRequest.PRIORITY_HIGH_ACCURACY)
                        } catch (e: Exception) {
                            Timber.e(e)
                        }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): RunController {
            return RunController()
        }
    }
}