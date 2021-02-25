package com.kumela.runn.ui.splash

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.kumela.runn.di.annotations.ScreenScope
import com.kumela.runn.ui.core.mvp.MvpBasePresenter
import com.kumela.runn.ui.core.navigation.ScreenNavigator
import javax.inject.Inject

@ScreenScope
class SplashPresenter @Inject constructor(
    private val screenNavigator: ScreenNavigator
) : MvpBasePresenter<SplashController>() {

    init {
        Handler(Looper.getMainLooper())
            .postDelayed({
                screenNavigator.toOnboarding()
            }, 400)
    }
}