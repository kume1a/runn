package com.kumela.rxbloc

internal interface Bloc<Event, State> :  StateConsumer<State>, EventDispatcher<Event>