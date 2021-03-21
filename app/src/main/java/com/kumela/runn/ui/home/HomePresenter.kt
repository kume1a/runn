package com.kumela.runn.ui.home

import com.kumela.mvx.mvp.MvpBasePresenter
import com.kumela.runn.data.db.run.RunSession
import com.kumela.runn.data.db.run.RunSessionService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import timber.log.Timber

class HomePresenter(
    private val runSessionService: RunSessionService,
) : MvpBasePresenter<HomeContract.View>(), HomeContract.Presenter {

    override fun onViewBound() {
        disposables.add(runSessionService.getAllRunSessions(8)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { runSessions ->
                ifViewAttached { view ->
                    view.bindRunSessions(runSessions)
                }
            }
        )
    }

    override fun onRunSessionClicked(runSession: RunSession) {
        Timber.d("onRunSessionClicked() called with: runSession = $runSession")
    }
}