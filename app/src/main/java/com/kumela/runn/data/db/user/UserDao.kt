package com.kumela.runn.data.db.user

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

/**
 * the user data access object
 *
 * supports only single user saving
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Completable

    @Update
    fun updateUser(user: User): Completable

    /**
     * @return user [User] that's registered in application
     */
    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): Maybe<User>
}
