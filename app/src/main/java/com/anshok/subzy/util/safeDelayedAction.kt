package com.anshok.subzy.util

import android.view.View
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

fun View.safeDelayedAction(delayMillis: Long, action: () -> Unit) {
    val lifecycleOwner = this.findViewTreeLifecycleOwner() ?: return

    lifecycleOwner.lifecycleScope.launch {
        delay(delayMillis)
        if (this@safeDelayedAction.isAttachedToWindow) {
            action()
        }
    }
}


