package com.kumela.dialogcontroller

import android.view.View

internal interface DialogRoot {
    fun getDialogContent(): View

    fun getBackgroundDimAmount(): Float
}