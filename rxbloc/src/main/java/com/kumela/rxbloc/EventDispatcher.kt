package com.kumela.rxbloc

internal fun interface EventDispatcher<Event> {
    fun dispatch(event: Event)
}