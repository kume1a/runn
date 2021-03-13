package com.kumela.runn.ui.run

import android.annotation.SuppressLint
import android.content.*
import android.content.res.Resources
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.marginBottom
import androidx.core.view.setPadding
import butterknife.BindView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.kumela.runn.BuildConfig
import com.kumela.runn.R
import com.kumela.runn.core.base.BaseController
import com.kumela.runn.core.fadeIn
import com.kumela.runn.core.fadeOut
import com.kumela.runn.core.translate
import com.kumela.runn.helpers.PermissionHandler
import timber.log.Timber
import javax.inject.Inject


@SuppressLint("NonConstantResourceId")
class RunController : BaseController<RunContract.View, RunContract.Presenter>(), RunContract.View {

    override val layoutRes: Int
        get() = R.layout.screen_run

    @BindView(R.id.map_view) lateinit var mapView: MapView
    @BindView(R.id.button_back) lateinit var buttonBack: ImageButton
    @BindView(R.id.card_content) lateinit var cardContent: CardView
    @BindView(R.id.text_time) lateinit var textTime: TextView
    @BindView(R.id.text_distance) lateinit var textDistance: TextView
    @BindView(R.id.text_pace) lateinit var textPace: TextView
    @BindView(R.id.text_calories) lateinit var textCalories: TextView
    @BindView(R.id.button_start_pause) lateinit var buttonStartPause: TextView
    @BindView(R.id.view_lock) lateinit var viewLock: View
    @BindView(R.id.button_resume) lateinit var buttonResume: ImageButton
    @BindView(R.id.text_resume) lateinit var textResume: TextView
    @BindView(R.id.button_stop) lateinit var buttonStop: ImageButton
    @BindView(R.id.text_stop) lateinit var textStop: TextView

    private var map: GoogleMap? = null

    @Inject lateinit var permissionHandler: PermissionHandler

    override fun onViewBound(view: View, savedViewState: Bundle?) {
        super.onViewBound(view, savedViewState)

        buttonBack.setOnClickListener { presenter.onBackClicked() }
        buttonStartPause.setOnClickListener { presenter.onStartPauseClicked() }
        buttonResume.setOnClickListener { presenter.onResumeClicked() }
        buttonStop.setOnClickListener { presenter.onStopClicked() }

        mapView.getMapAsync { googleMap ->
            map = googleMap
            try {
                map!!.isMyLocationEnabled = true
                map!!.uiSettings.isCompassEnabled = false
                map!!.uiSettings.isRotateGesturesEnabled = false
                map!!.setMaxZoomPreference(18f)
                val buttonMyLocation = mapView.findViewById<ImageView>("2".toInt())
                buttonMyLocation.setImageResource(R.drawable.ic_gps)
                buttonMyLocation.setPadding(getDimension(R.dimen.padding_my_location).toInt())
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

    override fun isLocationPermissionGranted(): Boolean = permissionHandler.isLocationPermissionGranted(activity!!)

    override fun shouldShowLocationPermissionRationale(): Boolean = permissionHandler.shouldShowLocationPermissionRationale(this)

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

    override fun isLocationEnabled(): Boolean {
        if (activity == null) return false

        val locationService = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationService.isProviderEnabled(LocationManager.GPS_PROVIDER)
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

    override fun setTime(time: String) {
        textTime.text = time
    }

    override fun getTextTime(): String = textTime.text.toString()

    override fun setDistance(km: String) {
        textDistance.text = km
    }

    override fun getTextDistance(): String = textDistance.text.toString()

    override fun setPace(pace: String) {
        textPace.text = pace
    }

    override fun getTextPace(): String = textPace.text.toString()

    override fun setCalories(calories: String) {
        textCalories.text = calories
    }

    override fun getTextCalories(): String = textCalories.text.toString()

    override fun changeStartToPause() {
        buttonStartPause.post {
            buttonStartPause.text = getString(R.string.pause)
            buttonStartPause.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause, 0, 0, 0)
        }
    }

    override fun showBlockingView() {
        viewLock.fadeIn()
    }

    override fun showResumeStop() {
        buttonResume.fadeIn()
        textResume.fadeIn()
        buttonStop.fadeIn()
        textStop.fadeIn()
        viewLock.fadeIn()
    }

    override fun hideResumeStop() {
        buttonResume.fadeOut()
        textResume.fadeOut()
        buttonStop.fadeOut()
        textStop.fadeOut()
        viewLock.fadeOut()
    }

    override fun showBottomButtons() {
        buttonStartPause.translate(0f)
        cardContent.translate(0f)
    }

    override fun hideBottomButtons() {
        val buttonTranslation = buttonStartPause.height.toFloat() + buttonStartPause.marginBottom
        val cardTranslation = buttonTranslation / 2f
        buttonStartPause.translate(buttonTranslation)
        cardContent.translate(cardTranslation)
    }

    override fun drawLine(a: LatLng, b: LatLng) {
        map?.addPolyline(PolylineOptions().add(a).add(b).color(getColor(R.color.accent)))
    }

    override fun moveCameraTo(position: LatLng, bearing: Float) {
        val cameraPosition = CameraPosition.Builder(map?.cameraPosition)
            .target(position)
            .zoom(16f)
            .bearing(bearing)
            .build()

        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun takeMapSnapshot(locationPoints: List<LatLng>) {
        @SuppressLint("MissingPermission")
        map?.isMyLocationEnabled = false
        map?.snapshot { bitmap -> presenter.onMapSnapshot(bitmap) }
    }

    override fun normalizeCamera(locationPoints: List<LatLng>) {
        val builder = LatLngBounds.Builder()
        locationPoints.forEach { point -> builder.include(point) }
        val bounds = builder.build()
        val update = CameraUpdateFactory.newLatLngBounds(bounds, 100)
        map?.moveCamera(update)
    }

    override fun plotAllLines(locationPoints: List<LatLng>) {
        val polyLines = PolylineOptions()
        locationPoints.forEach { point -> polyLines.add(point) }
        polyLines.color(getColor(R.color.accent))
        map?.addPolyline(polyLines)
    }

    override fun clearMap() {
        map?.clear()
    }

    companion object {
        @JvmStatic
        fun newInstance(): RunController {
            return RunController()
        }
    }
}