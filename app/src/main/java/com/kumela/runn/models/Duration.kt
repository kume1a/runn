package com.kumela.runn.models

data class Duration(val seconds: Int, val minutes: Int, val hours: Int) {
    fun format(): String {
        val hh = (if (hours < 10) "0" else "") + hours
        val mm = (if (minutes < 10) "0" else "") + minutes
        val ss = (if (seconds < 10) "0" else "") + seconds

        return "$hh:$mm:$ss"
    }

    val totalMinutes get() = hours * 60 + minutes
    val totalSeconds get() = hours * 3600 + minutes * 60 + seconds
}