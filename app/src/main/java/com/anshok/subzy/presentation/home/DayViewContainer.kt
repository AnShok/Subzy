package com.anshok.subzy.presentation.home

import android.view.View
import android.widget.TextView
import com.anshok.subzy.R
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.ViewContainer
import java.time.LocalDate

class DayViewContainer(view: View) : ViewContainer(view) {
    val dayText: TextView = view.findViewById(R.id.dayText)

    fun bind(day: CalendarDay, hasSubscription: Boolean, today: LocalDate) {
        dayText.text = day.date.dayOfMonth.toString()
        dayText.isSelected = day.date == today

        val color = when {
            day.position != DayPosition.MonthDate -> R.color.Gray_30
            hasSubscription -> R.color.Accent_S_50
            else -> R.color.White
        }

        dayText.setTextColor(view.context.getColor(color))
    }
}
