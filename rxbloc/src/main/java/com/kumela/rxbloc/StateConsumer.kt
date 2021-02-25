package com.kumela.rxbloc

import io.reactivex.rxjava3.core.Observable

internal interface StateConsumer<State> {
    val currentState: State
    val previousState: State

    val state: Observable<State>
}