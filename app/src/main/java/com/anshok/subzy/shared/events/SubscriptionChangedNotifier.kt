package com.anshok.subzy.shared.events

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SubscriptionChangedNotifier {
    private val _flow = MutableSharedFlow<Unit>(replay = 0)
    val flow = _flow.asSharedFlow()

    suspend fun notify() {
        _flow.emit(Unit)
    }
}
