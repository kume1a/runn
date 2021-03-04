package com.kumela.mvx.mvc

interface ObservableViewMvc<Listener> : ViewMvc {
    fun registerListener(listener: Listener)
    fun unregisterListener()
}