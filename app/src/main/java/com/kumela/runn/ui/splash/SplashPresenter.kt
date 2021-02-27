package com.kumela.runn.ui.splash

import android.os.Handler
import android.os.Looper
import com.kumela.runn.ui.core.mvp.MvpBasePresenter
import com.kumela.runn.ui.core.navigation.ScreenNavigator
import timber.log.Timber
import kotlin.system.exitProcess

class SplashPresenter constructor(
    private val screenNavigator: ScreenNavigator,
    model: SplashContract.Model,
) : MvpBasePresenter<SplashContract.View>(), SplashContract.Presenter {

    init {
        val disposable = model.getUser()
            .doOnEvent { user, error ->
                Timber.d("user = $user")
                if (error == null) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (user == null) {
                            screenNavigator.toOnboarding()
                        } else {
                            screenNavigator.toHome()
                        }
                    }, 400L)
                } else {
                    exitProcess(1)
                }
            }
            .subscribe()

        disposables.add(disposable)
    }
}