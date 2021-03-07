package com.kumela.runn.ui.run

import android.annotation.SuppressLint
import android.content.*
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.Button
import butterknife.BindView
import com.google.android.material.snackbar.Snackbar
import com.kumela.runn.BuildConfig
import com.kumela.runn.R
import com.kumela.runn.core.base.BaseController
import com.kumela.runn.helpers.PermissionHandler
import javax.inject.Inject


@SuppressLint("NonConstantResourceId")
class RunController : BaseController<RunContract.View, RunContract.Presenter>(), RunContract.View {

    override val layoutRes: Int
        get() = R.layout.screen_run

    @BindView(R.id.button_start) lateinit var buttonStart: Button
    @BindView(R.id.button_stop) lateinit var buttonStop: Button

    @Inject lateinit var permissionHandler: PermissionHandler

    override fun onViewBound(view: View) {
        super.onViewBound(view)

        buttonStart.setOnClickListener { presenter.onStartClicked() }
        buttonStop.setOnClickListener { presenter.onStopClicked() }
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