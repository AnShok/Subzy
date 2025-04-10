package com.anshok.subzy.presentation.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentCalendarBinding
import com.anshok.subzy.util.MonthUtils
import com.anshok.subzy.util.safeDelayedClick
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.MonthDayBinder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class CalendarFragment : Fragment() {


    private val binding: FragmentCalendarBinding by viewBinding(CreateMethod.INFLATE)
    private val today = LocalDate.now()

    private val subscriptionMap = mapOf(
        LocalDate.of(2025, 4, 2) to listOf("Netflix"),
        LocalDate.of(2025, 4, 9) to listOf("YouTube Premium", "GitHub Copilot"),
        LocalDate.of(2025, 4, 10) to listOf("Notion Pro"),
        LocalDate.of(2025, 4, 24) to listOf("Spotify"),

        LocalDate.of(2025, 5, 2) to listOf("Netflix"),
        LocalDate.of(2025, 5, 9) to listOf("YouTube Premium", "GitHub Copilot"),
        LocalDate.of(2025, 5, 10) to listOf("Notion Pro"),
        LocalDate.of(2025, 5, 24) to listOf("Spotify"),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backButton.safeDelayedClick {
            findNavController().navigateUp()
        }

        val calendarView = binding.calendarView
        val currentMonth = YearMonth.now()

        calendarView.setup(
            startMonth = currentMonth.minusMonths(12),
            endMonth = currentMonth.plusMonths(24),
            firstDayOfWeek = DayOfWeek.MONDAY
        )
        calendarView.scrollToMonth(currentMonth)

        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.bind(day, subscriptionMap.containsKey(day.date), today)
                container.view.setOnClickListener {
                    updateSubscriptionsForDate(day.date)
                    calendarView.notifyDayChanged(day)
                }
            }
        }

        calendarView.monthScrollListener = {
            val monthName =
                MonthUtils.getLocalizedMonthName(requireContext(), it.yearMonth.monthValue)
            val year = it.yearMonth.year
            binding.monthTitle.text = getString(R.string.month_title, monthName, year)
        }

        binding.prevMonthButton.safeDelayedClick {
            calendarView.findFirstVisibleMonth()?.let {
                calendarView.scrollToMonth(it.yearMonth.minusMonths(1))
            }
        }

        binding.nextMonthButton.safeDelayedClick {
            calendarView.findFirstVisibleMonth()?.let {
                calendarView.scrollToMonth(it.yearMonth.plusMonths(1))
            }
        }

        updateSubscriptionsForDate(today)
    }

    private fun updateSubscriptionsForDate(date: LocalDate) {
        val subs = subscriptionMap[date]
        binding.todaySubscriptionsSubtitle.text =
            if (subs.isNullOrEmpty()) "Нет подписок" else subs.joinToString(", ")
    }
}