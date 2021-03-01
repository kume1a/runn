package com.kumela.runn.ui.core.behaviors

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutLinearInInterpolator

@Suppress("unused")
class ScaleViewBehavior<V : View> : CoordinatorLayout.Behavior<V> {

    constructor() : super()
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private var currentScrollState = ScrollState.SCROLLED_UP
    private var currentAnimator: ViewPropertyAnimator? = null

    private val interpolator = FastOutLinearInInterpolator()

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int,
    ): Boolean = axes == ViewCompat.SCROLL_AXIS_VERTICAL

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray,
    ) {
        if (dyConsumed > 0) {
            hideView(child)
        } else if (dyConsumed < 0) {
            showView(child)
        }
    }

    private fun showView(child: V) {
        if (currentScrollState == ScrollState.SCROLLED_UP) return

        if (currentAnimator != null) {
            currentAnimator!!.cancel()
            child.clearAnimation()
        }

        currentScrollState = ScrollState.SCROLLED_UP
        animateChild(child, 1f, ENTER_ANIMATION_DURATION)
    }

    private fun hideView(child: V) {
        if (currentScrollState == ScrollState.SCROLLED_DOWN) return

        if (currentAnimator != null) {
            currentAnimator!!.cancel()
            child.clearAnimation()
        }

        currentScrollState = ScrollState.SCROLLED_DOWN
        animateChild(child, 0f, EXIT_ANIMATION_DURATION)
    }

    private fun animateChild(child: V, targetScale: Float, duration: Long) {
        currentAnimator = child.animate()
            .scaleX(targetScale)
            .scaleY(targetScale)
            .setInterpolator(interpolator)
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    currentAnimator = null
                }
            })
    }

    enum class ScrollState { SCROLLED_DOWN, SCROLLED_UP }

    companion object {
        private const val ENTER_ANIMATION_DURATION = 225L
        private const val EXIT_ANIMATION_DURATION = 175L
    }
}