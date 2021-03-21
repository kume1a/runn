package com.kumela.runn.data.db.run

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RunSessionService @Inject constructor(private val runSessionDao: RunSessionDao) {

    fun createRunSession(runSession: RunSession): Completable {
        return runSessionDao.insertRunSession(runSession)
            .subscribeOn(Schedulers.io())
    }

    fun deleteRunSession(runSession: RunSession): Completable {
        return runSessionDao.deleteRunSession(runSession)
            .subscribeOn(Schedulers.io())
    }

    fun getAllRunSessions(limit: Int = Int.MAX_VALUE): Observable<List<RunSession>> {
        return runSessionDao.getAllRunSessions(limit)
            .subscribeOn(Schedulers.io())
    }
}