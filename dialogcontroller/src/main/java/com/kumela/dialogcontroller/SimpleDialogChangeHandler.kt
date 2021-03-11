package com.kumela.dialogcontroller

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler

class SimpleDialogChangeHandler : AnimatorChangeHandler(150L, false) {

    override fun getAnimator(
        container: ViewGroup,
        from: View?,
        to: View?,
        isPush: Boolean,
        toAddedToContainer: Boolean,
    ): Animator {
        val animator = AnimatorSet()
        if (to != null) {
            val start: Float = if (toAddedToContainer) 0f else to.alpha
            animator.play(ObjectAnimator.ofFloat(to, View.ALPHA, start, 1f))
        }

        if (from != null && (!isPush || removesFromViewOnPush())) {
            animator.play(ObjectAnimator.ofFloat(from, View.ALPHA, 0f))
        }

        return animator
    }

    override fun resetFromView(from: View) {
        from.alpha = 1f
    }
}