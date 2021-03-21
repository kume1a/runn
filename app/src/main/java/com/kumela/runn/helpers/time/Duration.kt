package com.kumela.runn.helpers.time

import kotlin.math.floor

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Duration private constructor(private val value: Long) {

    /**
     * unsafe constructor
     * [Long] can be overflowed if not careful
     */
    constructor(
        days: Long = 0,
        hours: Long = 0,
        minutes: Long = 0,
        seconds: Long = 0,
        milliseconds: Long = 0,
        microseconds: Long = 0,
    ) : this(microsecondsPerDay * days +
            microsecondsPerHour * hours +
            microsecondsPerMinute * minutes +
            microsecondsPerSecond * seconds +
            microsecondsPerMillisecond * milliseconds
            + microseconds)

    val inDays get() = (value / microsecondsPerDay).floor()
    val inHours get() = (value / microsecondsPerHour).floor()
    val inMinutes get() = (value / microsecondsPerMinute).floor()
    val inSeconds get() = (value / microsecondsPerSecond).floor()
    val inMilliseconds get() = (value / microsecondsPerMillisecond).floor()
    val inMicroseconds get() = value

    fun toSMH(): String {
        val hh = (if (inHours < 10) "0" else "") + inHours
        val mm = (if (inMinutes < 10) "0" else "") + inMinutes
        val ss = (if (inSeconds < 10) "0" else "") + inSeconds

        return "$hh:$mm:$ss"
    }

    private fun Long.floor(): Long = floor(toDouble()).toLong()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Duration

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int = value.hashCode()

    companion object {
        const val microsecondsPerMillisecond = 1000L
        const val millisecondsPerSecond = 1000L
        const val secondsPerMinute = 60L
        const val minutesPerHour = 60L
        const val hoursPerDay = 24L

        const val microsecondsPerSecond = microsecondsPerMillisecond * millisecondsPerSecond
        const val microsecondsPerMinute = microsecondsPerSecond * secondsPerMinute
        const val microsecondsPerHour = microsecondsPerMinute * minutesPerHour
        const val microsecondsPerDay = microsecondsPerHour * hoursPerDay

        const val millisecondsPerMinute = millisecondsPerSecond * secondsPerMinute
        const val millisecondsPerHour = millisecondsPerMinute * minutesPerHour
        const val millisecondsPerDay = millisecondsPerHour * hoursPerDay

        const val secondsPerHour = secondsPerMinute * minutesPerHour
        const val secondsPerDay = secondsPerHour * hoursPerDay

        const val minutesPerDay = minutesPerHour * hoursPerDay

        val zero = Duration()
    }
}
