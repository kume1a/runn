package com.kumela.dialogcontroller

import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction

fun Router.pushDialog(dialog: DialogController) {
    pushController(
        RouterTransaction.with(dialog)
            .pushChangeHandler(SimpleDialogChangeHandler())
            .popChangeHandler(SimpleDialogChangeHandler())
    )
}
