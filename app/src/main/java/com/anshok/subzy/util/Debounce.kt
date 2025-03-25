package com.anshok.subzy.util

import kotlinx.coroutines.*

class DebounceWrapper<T>(
    private val waitMs: Long,
    private val coroutineScope: CoroutineScope,
    private val destinationFunction: (T) -> Unit
) {
    private var debounceJob: Job? = null

    operator fun invoke(param: T) {
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(waitMs)
            destinationFunction(param)
        }
    }

    fun cancel() {
        debounceJob?.cancel()
    }
}

fun <T> debounce(
    waitMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit
): DebounceWrapper<T> {
    return DebounceWrapper(waitMs, coroutineScope, destinationFunction)
}
