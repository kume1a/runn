package com.kumela.runn.data.db

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

abstract class DbService {
    protected fun runDbOp(action: Action) {
        Completable.fromAction(action)
            .subscribeOn(Schedulers.io())
            .subscribe({}, { error ->
                Timber.e(error, "runDbOp: error")
            })
    }
}