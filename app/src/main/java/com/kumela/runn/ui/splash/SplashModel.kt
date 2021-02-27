package com.kumela.runn.ui.splash

import com.kumela.runn.data.db.user.User
import com.kumela.runn.data.db.user.UserService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe

class SplashModel(private val userService: UserService) : SplashContract.Model {
    override fun getUser(): Maybe<User> {
        return userService.getUser()
            .observeOn(AndroidSchedulers.mainThread())
    }
}