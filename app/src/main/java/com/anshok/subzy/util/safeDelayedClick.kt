package com.anshok.subzy.util

import android.view.View
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

fun View.safeDelayedClick(delayMillis: Long = 1000L, action: (View) -> Unit) {
    this.setOnClickListener { view ->
        view.isEnabled = false
        action(view)

        val lifecycleOwner = view.findViewTreeLifecycleOwner()
        lifecycleOwner?.lifecycleScope?.launch {
            delay(delayMillis)
            view.isEnabled = true
        }
    }
}
