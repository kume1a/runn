package com.kumela.runn.core.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import butterknife.BindView
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.kumela.runn.R
import com.kumela.runn.core.base.BaseActivity
import com.kumela.runn.notifications.AppNotificationManager
import com.kumela.runn.ui.home.HomeController
import com.kumela.runn.ui.plans.PlansController
import com.kumela.runn.ui.profile.ProfileController
import com.kumela.runn.ui.splash.SplashController
import com.kumela.runn.ui.statistics.StatisticsController
import com.kumela.views.BottomNav
import com.kumela.views.DiamondFab
import com.kumela.views.listeners.OnItemSelectedListener

@SuppressLint("NonConstantResourceId")
class MainActivity : BaseActivity() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    override val initialScreen: Controller
        get() = SplashController()

    @BindView(R.id.bottom_nav) lateinit var bottomNav: BottomNav
    @BindView(R.id.diamond_fab) lateinit var diamondFab: DiamondFab

    private val interpolator = AccelerateDecelerateInterpolator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val launchedFromNotification = intent.getBooleanExtra(AppNotificationManager.EXTRA_STARTED_FROM_NOTIFICATION, false)
        if (launchedFromNotification) {
            Handler(Looper.getMainLooper()).postDelayed({screenNavigator.toRun()}, 1300L)
        }

        handleBottomNavigation(router.backstack.last().controller)

        diamondFab.setOnClickListener { screenNavigator.toRun() }
        bottomNav.onItemSelectedListener = OnItemSelectedListener { index ->
            when (index) {
                0 -> screenNavigator.toHome()
                1 -> screenNavigator.toPlans()
                2 -> screenNavigator.toStatistics()
                3 -> screenNavigator.toProfile()
            }
        }
    }

    override fun onScreenChangeStarted(
        to: Controller?,
        from: Controller?,
        isPush: Boolean,
        container: ViewGroup,
        handler: ControllerChangeHandler,
    ) {
        handleBottomNavigation(to)
    }

    private fun handleBottomNavigation(controller: Controller?) {
        when (controller) {
            is HomeController, is PlansController, is StatisticsController, is ProfileController -> showBottomNavigation()
            else -> hideBottomNavigation()
        }
    }

    private fun showBottomNavigation() {
        bottomNav.visibility = View.VISIBLE
        diamondFab.visibility = View.VISIBLE

        translateViewTo(bottomNav, 0f, ENTER_ANIMATION_DURATION)
        scaleViewTo(diamondFab, 1f, ENTER_ANIMATION_DURATION)
    }

    private fun hideBottomNavigation() {
        scaleViewTo(diamondFab, 0f, EXIT_ANIMATION_DURATION) { diamondFab.visibility = View.GONE }
        translateViewTo(bottomNav, bottomNav.height.toFloat(), EXIT_ANIMATION_DURATION) { bottomNav.visibility = View.GONE }
    }

    private fun translateViewTo(
        view: View,
        translationY: Float,
        duration: Long,
        onAnimationEnd: (() -> Unit)? = null,
    ) {
        view.animate()
            .translationY(translationY)
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    onAnimationEnd?.invoke()
                }
            })
            .setInterpolator(interpolator)
            .start()
    }

    private fun scaleViewTo(
        view: View,
        scale: Float,
        duration: Long,
        onAnimationEnd: (() -> Unit)? = null,
    ) {
        view.animate()
            .scaleX(scale)
            .scaleY(scale)
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    onAnimationEnd?.invoke()
                }
            })
            .setInterpolator(interpolator).start()
    }

    companion object {
        private const val ENTER_ANIMATION_DURATION = 225L
        private const val EXIT_ANIMATION_DURATION = 175L
    }
}