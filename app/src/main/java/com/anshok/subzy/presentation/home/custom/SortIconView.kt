package com.anshok.subzy.presentation.home.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.anshok.subzy.R

/**
 * Кастомный вью-компонент для отображения стрелки сортировки (вверх/вниз) с анимацией.
 *
 * Используется в BottomSheet сортировки для визуального отображения направления сортировки.
 * Содержит две иконки: `iconUp` и `iconDown`, между которыми переключается с плавной анимацией.
 */
class SortIconView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val iconDown: ImageView
    private val iconUp: ImageView

    private var isAsc = true    // Текущее направление сортировки
    private var isFirstSet = true   // Флаг для подавления анимации при первом отображении

    init {
        LayoutInflater.from(context).inflate(R.layout.view_sort_icon, this, true)
        iconDown = findViewById(R.id.iconDown)
        iconUp = findViewById(R.id.iconUp)
        iconDown.visibility = GONE
        iconUp.visibility = GONE
    }

    /**
     * Устанавливает направление сортировки и отображает соответствующую иконку.
     *
     * @param isAscending true – сортировка по возрастанию, false – по убыванию
     * @param animate true – применить анимацию переключения, false – без анимации
     */
    fun setDirection(isAscending: Boolean, animate: Boolean = true) {
        val fromView = if (isAsc) iconDown else iconUp
        val toView = if (isAscending) iconUp else iconDown

        isAsc = isAscending

        if (!animate || isFirstSet) {
            // Первая установка — сразу без анимации
            iconDown.visibility = GONE
            iconUp.visibility = GONE

            val active = if (isAsc) iconUp else iconDown
            active.visibility = VISIBLE
            active.alpha = 1f
            active.translationY = 0f

            isFirstSet = false
            return
        }

        // Анимация скрытия старой иконки
        val inFromY = if (isAsc) 30f else -30f
        val outToY = if (isAsc) -30f else 30f

        fromView.animate()
            .translationY(outToY)
            .alpha(0f)
            .setDuration(200)
            .withEndAction { fromView.visibility = GONE }
            .start()

        // Анимация появления новой иконки
        toView.translationY = inFromY
        toView.alpha = 0f
        toView.visibility = VISIBLE
        toView.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(200)
            .start()
    }

    /**
     * Показывает текущую иконку с возможной анимацией.
     */
    fun show(animate: Boolean = true) {
        val active = if (isAsc) iconDown else iconUp
        if (animate) {
            val fromY = if (isAsc) -30f else 30f
            active.translationY = fromY
            active.alpha = 0f
            active.visibility = VISIBLE
            active.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(200)
                .start()
        } else {
            active.visibility = VISIBLE
            active.alpha = 1f
            active.translationY = 0f
        }
    }

    /**
     * Скрывает текущую иконку с возможной анимацией.
     */
    fun hide(animate: Boolean = true) {
        val active = if (isAsc) iconDown else iconUp
        if (animate) {
            val toY = if (isAsc) -30f else 30f
            active.animate()
                .alpha(0f)
                .translationY(toY)
                .setDuration(150)
                .withEndAction { active.visibility = GONE }
                .start()
        } else {
            active.visibility = GONE
        }
    }
}
