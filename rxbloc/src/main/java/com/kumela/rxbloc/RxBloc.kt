package com.kumela.rxbloc

import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class RxBloc<Event, State>(initialState: State) : Bloc<Event, State> {
    override val currentState: State
        get() = _state.value

    private var _previousState: State = initialState
    override val previousState: State
        get() = _previousState

    private val _state = BehaviorRelay.createDefault(initialState)

    override val state: Observable<State>
        get() = _state.subscribeOn(Schedulers.io())

    abstract fun mapEventToState(event: Event)

    override fun dispatch(event: Event) {
        mapEventToState(event)
    }

    protected fun yield(state: State) {
        _previousState = currentState
        _state.accept(state)
    }
}