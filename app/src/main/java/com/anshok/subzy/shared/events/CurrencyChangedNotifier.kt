package com.anshok.subzy.shared.events

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CurrencyChangedNotifier {
    private val _flow = MutableSharedFlow<String>(replay = 0)
    val flow = _flow.asSharedFlow()

    suspend fun notify(code: String) {
        _flow.emit(code)
    }
}
