package com.kumela.runn.core

import android.view.View

fun Double.roundToSingleDecimal(): Double = String.format("%.1f", this).toDouble()
fun Float.roundToSingleDecimal(): Float = String.format("%.1f", this).toFloat()

fun View.translate(translationY: Float, duration: Long = 300L) {
    animate()
        .translationY(translationY)
        .setDuration(duration)
        .start()
}

fun View.fadeIn() {
    fade(this, 1f)
}

fun View.fadeOut() {
    fade(this, 0f)
}

private fun fade(v: View, alpha: Float) {
    v.animate()
        .withStartAction { if (alpha == 1f) v.visibility = View.VISIBLE }
        .withEndAction { if (alpha == 0f) v.visibility = View.GONE }
        .alpha(alpha)
        .setDuration(400L)
        .start()
}
