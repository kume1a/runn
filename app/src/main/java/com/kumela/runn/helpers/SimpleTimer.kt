package com.kumela.runn.helpers

import com.kumela.runn.models.Duration
import kotlin.math.floor

abstract class SimpleTimer : PausableCountDownTimer(Long.MAX_VALUE, 1000L) {

    override fun onTick(millisUntilFinished: Long) {
        val millis = Long.MAX_VALUE - millisUntilFinished
        val timeInSeconds = millis / 1000
        onTick(formatDuration(timeInSeconds))
    }

    private fun formatDuration(timeInSeconds: Long): Duration {
        val seconds: Int = (timeInSeconds % 3600 % 60).toInt()
        val minutes: Int = floor((timeInSeconds % 3600 / 60).toDouble()).toInt()
        val hours: Int = floor((timeInSeconds / 3600).toDouble()).toInt()

        return Duration(seconds, minutes, hours)
    }

    abstract fun onTick(duration: Duration)

    override fun onFinish() {}
}