package com.kumela.runn.ui.home

import com.kumela.mvx.mvp.core.MvpPresenter
import com.kumela.mvx.mvp.core.MvpView
import com.kumela.runn.data.db.run.RunSession


interface HomeContract {
    interface View: MvpView {
        fun bindRunSessions(runSessions: List<RunSession>)
    }

    interface Presenter: MvpPresenter<View> {
        fun onRunSessionClicked(runSession: RunSession)
    }
}