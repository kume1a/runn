package com.kumela.runn.core.lifecycle

import android.app.Activity
import android.content.Context
import android.view.View

abstract class ScreenLifecycleTask {

    open fun onContextAvailable(context: Context, activity: Activity?) {}

    open fun onEnterScope(view: View?) {}

    open fun onExitScope(view: View?) {}

    open fun onDestroy() {}

    open fun onActivityPaused(activity: Activity) {}

    open fun onActivityResumed(activity: Activity) {}

    open fun onActivityStarted(activity: Activity) {}

    open fun onActivityStopped(activity: Activity) {}
}