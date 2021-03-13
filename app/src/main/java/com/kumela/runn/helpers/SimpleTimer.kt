package com.kumela.runn.helpers

import com.kumela.runn.core.toDuration
import com.kumela.runn.models.Duration

abstract class SimpleTimer : PausableCountDownTimer(Long.MAX_VALUE, 1000L) {

    override fun onTick(millisUntilFinished: Long) {
        val millis = Long.MAX_VALUE - millisUntilFinished
        val timeInSeconds = millis / 1000
        onTick(timeInSeconds.toDuration())
    }

    abstract fun onTick(duration: Duration)

    override fun onFinish() {}
}