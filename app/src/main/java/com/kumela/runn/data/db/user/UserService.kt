package com.kumela.runn.data.db.user

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserService @Inject constructor(private val userDao: UserDao) {

    fun getUser(): Maybe<User> {
        return userDao.getUser()
            .subscribeOn(Schedulers.io())
    }

    fun createUser(user: User): Completable {
        return userDao.insertUser(user)
            .subscribeOn(Schedulers.io())

    }

    fun updateUser(user: User): Completable {
        return userDao.updateUser(user)
            .subscribeOn(Schedulers.io())
    }
}