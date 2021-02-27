package com.kumela.runn.ui.onboarding

import com.kumela.runn.data.db.user.User
import com.kumela.runn.data.db.user.UserService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable

class OnboardingModel(private val userService: UserService) : OnboardingContract.Model {

    override fun createUser(user: User): Completable {
        return userService.createUser(user)
            .observeOn(AndroidSchedulers.mainThread())
    }
}