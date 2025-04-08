package com.anshok.subzy.util.animation

import android.view.View
import androidx.fragment.app.Fragment

private const val DEFAULT_ANIM_DURATION = 300L

/**
 * Плавное появление View с альфой.
 * Использовать, когда элемент показывается впервые.
 *
 * @param hasAnimated Флаг, был ли уже показан с анимацией.
 * @param duration Длительность анимации в мс.
 * @param onAnimated Колбэк после завершения анимации.
 */
fun View.fadeInOnce(
    hasAnimated: Boolean,
    duration: Long = DEFAULT_ANIM_DURATION,
    onAnimated: () -> Unit = {}
) {
    if (!hasAnimated) {
        alpha = 0f
        visibility = View.VISIBLE
        animate()
            .alpha(1f)
            .setDuration(duration)
            .withEndAction { onAnimated() }
            .start()
    } else {
        visibility = View.VISIBLE
    }
}

/**
 * Мягкая альфа-анимация с вертикальным сдвигом (например, для первого элемента списка).
 */
fun View.fadeInWithTranslation(
    translationY: Float = 20f,
    duration: Long = DEFAULT_ANIM_DURATION,
    delay: Long = 0
) {
    alpha = 0f
    this.translationY = translationY
    postDelayed({
        animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(duration)
            .start()
    }, delay)
}

fun Fragment.animateEntry(targetView: View, delay: Long = 100L) {
    targetView.apply {
        alpha = 0f
        translationY = 40f
        postDelayed({
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300L)
                .start()
        }, delay)
    }
}

fun View.animateAppear(
    startAlpha: Float = 0f,
    startTranslationY: Float = 40f,
    duration: Long = DEFAULT_ANIM_DURATION,
    delay: Long = 0L
) {
    alpha = startAlpha
    translationY = startTranslationY
    postDelayed({
        animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(duration)
            .start()
    }, delay)
}



