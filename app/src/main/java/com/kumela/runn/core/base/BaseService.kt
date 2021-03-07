package com.kumela.runn.core.base

import android.app.Service
import com.kumela.runn.di.service.ServiceModule

abstract class BaseService: Service() {

    protected val injector by lazy {
        (application as BaseApplication).appComponent.newServiceComponent(ServiceModule(this))
    }
}