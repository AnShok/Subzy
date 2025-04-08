package com.anshok.subzy.presentation.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentCalendarBinding
import com.anshok.subzy.domain.subscription.model.SubscriptionGroup
import com.anshok.subzy.presentation.calendar.groupie.*
import com.anshok.subzy.util.MonthUtils
import com.anshok.subzy.util.safeDelayedClick
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.MonthDayBinder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

/**
 * Фрагмент календаря, отображающий подписки по датам с возможностью навигации по месяцам.
 *
 * Основные возможности:
 * - Календарь с выбором дат на основе библиотеки Kizitonwose.
 * - Группировка подписок по дням или за месяц.
 * - Адаптер на базе Groupie с секциями: [GroupedSubscriptionSection], [SubscriptionItem], [SubscriptionHeaderItem].
 * - Плейсхолдер при отсутствии подписок на выбранную дату.
 * - Кнопка возврата к текущей дате.
 *
 * Использует ViewModel [CalendarViewModel] и ViewBinding [FragmentCalendarBinding].
 */
class CalendarFragment : Fragment() {

    private val binding: FragmentCalendarBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: CalendarViewModel by viewModel()

    private val today = LocalDate.now()
    private var selectedDate: LocalDate = today
    private var showDayMode = false
    private var lastVisibleMonth: YearMonth? = null


    private val adapter = CalendarGroupieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backButton.safeDelayedClick {
            findNavController().navigateUp()
        }

        binding.calendarSubscriptionList.adapter = adapter
        binding.calendarSubscriptionList.layoutManager = LinearLayoutManager(requireContext())

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
                val hasSubs = viewModel.hasSubscriptionsOn(day.date)

                container.bind(day, hasSubs, today, selectedDate)

                container.view.setOnClickListener {
                    selectedDate = day.date
                    showDayMode = true
                    calendarView.notifyCalendarChanged()
                    updateList()

                    // Показываем кнопку возврата
                    binding.returnTodayButton.visibility = View.VISIBLE
                }
            }
        }

        calendarView.monthScrollListener = { month ->
            val newMonth = month.yearMonth

            if (lastVisibleMonth == null || lastVisibleMonth != newMonth) {
                animateMonthTitleScroll(lastVisibleMonth ?: newMonth, newMonth)
                lastVisibleMonth = newMonth
            }

            // Показываем кнопку возврата, если не текущий месяц
            binding.returnTodayButton.visibility =
                if (newMonth == YearMonth.now() && !showDayMode) View.GONE else View.VISIBLE

            if (!showDayMode) updateList()
            calendarView.notifyCalendarChanged()
        }

        binding.returnTodayButton.safeDelayedClick {
            val currentMonth = YearMonth.now()
            val fromMonth = binding.calendarView.findFirstVisibleMonth()?.yearMonth ?: currentMonth

            // Плавный скролл
            binding.calendarView.smoothScrollToMonth(currentMonth)

            // Анимация заголовока месяца
            animateMonthTitleScroll(fromMonth, currentMonth, delayMs = 25L)

            // Сброс на текущую дату
            selectedDate = today
            showDayMode = false
            binding.returnTodayButton.visibility = View.GONE

            binding.calendarView.notifyCalendarChanged()
            updateList()
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.upcomingBills.collectLatest {
                calendarView.notifyCalendarChanged()
                updateList()
            }
        }
    }

    private fun updateMonthTitle(yearMonth: YearMonth) {
        val monthName = MonthUtils.getLocalizedMonthName(requireContext(), yearMonth.monthValue)
        binding.monthTitle.text = getString(R.string.month_title, monthName, yearMonth.year)
    }

    private fun updateList() {
        val groups: List<SubscriptionGroup> = if (showDayMode) {
            listOf(SubscriptionGroup(selectedDate, viewModel.getSubscriptionsForDate(selectedDate)))
        } else {
            val visibleMonth =
                binding.calendarView.findFirstVisibleMonth()?.yearMonth ?: YearMonth.now()
            viewModel.getGroupedSubscriptionsForMonth(visibleMonth)
        }

        val isEmpty = groups.all { it.subscriptions.isEmpty() }
        binding.calendarSubscriptionList.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.placeholderGroup.visibility = if (isEmpty) View.VISIBLE else View.GONE

        binding.calendarSubscriptionList.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_fade_slide)

        adapter.clear()
        groups.forEach { group ->
            val header = SubscriptionHeaderItem(group.date)
            val subs = group.subscriptions.map { sub ->
                SubscriptionItem(sub) {
                    val action =
                        CalendarFragmentDirections.actionCalendarFragmentToDetailsSubFragment(it.id)
                    findNavController().navigate(action)
                }
            }
            adapter.add(GroupedSubscriptionSection(header, subs))
        }

        binding.calendarSubscriptionList.scheduleLayoutAnimation()
    }

    private fun animateMonthTitleScroll(from: YearMonth, to: YearMonth, delayMs: Long = 30L) {
        lifecycleScope.launch {
            val step = if (to > from) 1 else -1
            var current = from

            while (current != to) {
                updateMonthTitle(current)
                current = current.plusMonths(step.toLong())
                delay(delayMs)
            }

            updateMonthTitle(to)
        }
    }
}
