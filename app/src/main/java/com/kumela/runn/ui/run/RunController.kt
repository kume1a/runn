package com.kumela.runn.ui.run

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.net.Uri
import android.os.IBinder
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import butterknife.BindView
import com.google.android.material.snackbar.Snackbar
import com.kumela.runn.BuildConfig
import com.kumela.runn.R
import com.kumela.runn.core.base.BaseController
import com.kumela.runn.helpers.PermissionHandler
import com.kumela.runn.services.LocationBroadcastReceiver
import com.kumela.runn.services.LocationUpdateService
import javax.inject.Inject


@SuppressLint("NonConstantResourceId")
class RunController : BaseController<RunContract.View, RunContract.Presenter>(), RunContract.View {

    override val layoutRes: Int
        get() = R.layout.screen_run

    private lateinit var receiver: LocationBroadcastReceiver
    private var bound = false
    private var service: LocationUpdateService? = null

    @BindView(R.id.button_start) lateinit var buttonStart: Button
    @BindView(R.id.button_stop) lateinit var buttonStop: Button

    @Inject lateinit var permissionHandler: PermissionHandler

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: LocationUpdateService.LocalBinder = service as LocationUpdateService.LocalBinder
            this@RunController.service = binder.getService()
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            this@RunController.service = null
            bound = false
        }
    }

    override fun onContextAvailable(context: Context) {
        super.onContextAvailable(context)

        receiver = LocationBroadcastReceiver()
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)

        buttonStart.setOnClickListener { presenter.onStartClicked() }
        buttonStop.setOnClickListener { presenter.onStopClicked() }

        activity?.bindService(Intent(activity, LocationUpdateService::class.java), mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onActivityStarted(activity: Activity) {
        super.onActivityStarted(activity)
        activity.bindService(Intent(activity, LocationUpdateService::class.java), mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver,
            IntentFilter(LocationUpdateService.ACTION_BROADCAST))
    }

    override fun onActivityPaused(activity: Activity) {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver)
        super.onActivityPaused(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        if (bound) {
            activity.unbindService(mServiceConnection)
            bound = false
        }
        super.onActivityStopped(activity)
    }

    //    --------------------------------------------------------------------------------------------------------------------------------

    override fun startService() {
        service?.requestLocationUpdates()
    }

    override fun endService() {
        service?.removeLocationUpdates()
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

    companion object {
        @JvmStatic
        fun newInstance(): RunController {
            return RunController()
        }
    }
}