package com.anshok.subzy.presentation.calendar

import android.view.View
import android.widget.TextView
import com.anshok.subzy.R
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate

/**
 * Контейнер для отображения одного дня в календаре на экране [CalendarFragment].
 *
 * Отвечает за установку текста, фона и цвета текста в зависимости от:
 * - выбранной даты,
 * - текущей даты,
 * - наличия подписок,
 * - позиции дня (внутри или вне текущего месяца).
 *
 * Используется в качестве dayBinder'а в [com.kizitonwose.calendar.view.CalendarView].
 *
 * @param view View элемента дня, содержащая [TextView] с ID `dayText`.
 */
class DayViewContainer(view: View) : ViewContainer(view) {
    val dayText: TextView = view.findViewById(R.id.dayText)

    fun bind(
        day: CalendarDay,
        hasSubscription: Boolean,
        today: LocalDate,
        selectedDate: LocalDate
    ) {
        dayText.text = day.date.dayOfMonth.toString()

        val isToday = day.date == today
        val isSelected = day.date == selectedDate

        val bgRes = when {
            isSelected -> R.drawable.bg_selected_day
            isToday -> R.drawable.bg_today_day
            else -> android.R.color.transparent
        }

        dayText.setBackgroundResource(bgRes)

        val textColor = when {
            day.position != DayPosition.MonthDate -> R.color.Gray_30
            hasSubscription -> R.color.Accent_S_50
            else -> R.color.White
        }

        dayText.setTextColor(view.context.getColor(textColor))
    }

}
