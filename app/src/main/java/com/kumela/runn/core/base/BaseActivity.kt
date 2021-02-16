package com.kumela.runn.core.base

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.Router
import com.kumela.runn.R
import com.kumela.runn.di.injectors.Injector
import com.kumela.runn.di.injectors.ScreenInjector
import com.kumela.runn.ui.navigation.ScreenNavigator
import java.util.*
import javax.inject.Inject


abstract class BaseActivity : AppCompatActivity() {

    lateinit var instanceId: String

    private lateinit var router: Router

    @Inject lateinit var screenInjector: ScreenInjector
    @Inject lateinit var screenNavigator: ScreenNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        instanceId = if (savedInstanceState != null) {
            savedInstanceState.getString(KEY_INSTANCE_ID)!!
        } else {
            UUID.randomUUID().toString()
        }

        Injector.inject(this)
        setContentView(layoutRes)

        val screenContainer = findViewById<FrameLayout>(R.id.screen_container)
            ?: throw NullPointerException("activity must have a view with id: screen_container")

        router = Conductor.attachRouter(this, screenContainer, savedInstanceState)
        screenNavigator.initializeWithRouter(router, initialScreen)
        monitorBackStack()

        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_INSTANCE_ID, instanceId)
    }

    override fun onBackPressed() {
        if (!screenNavigator.pop()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            Injector.clear(this)
        }
    }

    @get:LayoutRes
    protected abstract val layoutRes: Int

    protected abstract val initialScreen: Controller

    private fun monitorBackStack() {
        router.addChangeListener(object : ControllerChangeHandler.ControllerChangeListener {
            override fun onChangeStarted(
                to: Controller?,
                from: Controller?,
                isPush: Boolean,
                container: ViewGroup,
                handler: ControllerChangeHandler
            ) {}

            override fun onChangeCompleted(
                to: Controller?,
                from: Controller?,
                isPush: Boolean,
                container: ViewGroup,
                handler: ControllerChangeHandler
            ) {
                if (!isPush && from != null) {
                    Injector.clear(from)
                }
            }
        })
    }

    companion object {
        private const val KEY_INSTANCE_ID = "key_instance_id"
    }
}