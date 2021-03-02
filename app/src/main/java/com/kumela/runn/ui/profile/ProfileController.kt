package com.kumela.runn.ui.profile

import com.kumela.runn.R
import com.kumela.runn.core.base.BaseController

class ProfileController: BaseController<ProfileContract.View, ProfileContract.Presenter>(), ProfileContract.View {

    override val layoutRes: Int
        get() = R.layout.screen_profile

    companion object {
        @JvmStatic
        fun newInstance() : ProfileController {
            return ProfileController()
        }
    }
}