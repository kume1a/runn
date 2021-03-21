package com.kumela.runn.data.db.run

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface RunSessionDao {

    @Insert
    fun insertRunSession(runSession: RunSession): Completable

    @Delete
    fun deleteRunSession(runSession: RunSession): Completable

    @Query("SELECT * FROM run_sessions LIMIT :limit")
    fun getAllRunSessions(limit: Int): Observable<List<RunSession>>
}