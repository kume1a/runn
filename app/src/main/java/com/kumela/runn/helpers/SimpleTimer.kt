package com.kumela.runn.helpers

import com.kumela.runn.helpers.time.Duration

abstract class SimpleTimer : PausableCountDownTimer(Long.MAX_VALUE, 1000L) {

    override fun onTick(millisUntilFinished: Long) {
        val millis = Long.MAX_VALUE - millisUntilFinished
        onTick(Duration(milliseconds = millis))
    }

    abstract fun onTick(duration: Duration)

    override fun onFinish() {}
}