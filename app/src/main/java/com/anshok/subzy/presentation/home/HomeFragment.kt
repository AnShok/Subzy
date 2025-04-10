package com.anshok.subzy.presentation.home

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentHomeBinding
import com.anshok.subzy.presentation.home.adapter.MySubAdapter
import com.anshok.subzy.presentation.home.bottomsheet.SortBottomSheet
import com.anshok.subzy.presentation.home.bottomsheet.SortDirection
import com.anshok.subzy.presentation.home.bottomsheet.SortOption
import com.anshok.subzy.presentation.home.viewmodel.MySubViewModel
import com.anshok.subzy.util.CurrencyUtils
import com.anshok.subzy.util.animation.animateAppear
import com.anshok.subzy.util.animation.fadeInWithTranslation
import com.anshok.subzy.util.safeDelayedAction
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: MySubViewModel by viewModel()

    private lateinit var adapter: MySubAdapter
    private var hasAnimatedList = false
    private var hasAnimatedMetrics = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInitialAnimations()
        setupRecyclerView()
        observeMetrics()
        observeSubscriptions()
        observeSortState()
        setupMetricClicks()
        setupButtonsClicks()
    }

    private fun setupInitialAnimations() {
        binding.root.alpha = 0f
        binding.root.animate().alpha(1f).setDuration(250).start()

        binding.activeSubsCount.safeDelayedAction(2000) {
            binding.activeSubsCount.isSelected = true
            binding.highestSubsAmount.isSelected = true
            binding.lowestSubsAmount.isSelected = true
        }
    }

    private fun setupRecyclerView() {
        adapter = MySubAdapter { subscription ->
            val action =
                HomeFragmentDirections.actionHomeFragmentToDetailsSubFragment(subscription.id)
            findNavController().navigate(action)
        }

        binding.subscriptionsList.layoutManager = LinearLayoutManager(requireContext())
        binding.subscriptionsList.adapter = adapter

        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
    }

    /**
     * Подписка на метрики: общая сумма, самая дорогая и дешёвая подписка
     * Отображение и анимация при первом показе
     */
    private fun observeMetrics() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.metrics.collectLatest { metrics ->
                val currency = viewModel.getDefaultCurrencyCode()
                binding.statsTitle.text = metrics.period.displayText()

                if (metrics.highest == null && metrics.lowest == null) {
                    binding.activeSubsCount.text = "--"
                    binding.highestSubsAmount.text = "--"
                    binding.lowestSubsAmount.text = "--"
                    return@collectLatest
                }

                updateMetricText(binding.activeSubsCount, metrics.totalFormatted, currency)
                metrics.highest?.let {
                    updateMetricText(binding.highestSubsAmount, it.second, currency)
                } ?: run { binding.highestSubsAmount.text = "--" }

                metrics.lowest?.let {
                    updateMetricText(binding.lowestSubsAmount, it.second, currency)
                } ?: run { binding.lowestSubsAmount.text = "--" }

                if (!hasAnimatedMetrics) {
                    hasAnimatedMetrics = true
                    binding.metricsCard.animateAppear()
                }
            }
        }
    }

    /**
     * Обновление текста метрики с анимацией
     */
    private fun updateMetricText(view: TextView, value: Double, currency: String) {
        val current = view.text.toString().replace("[^\\d.,]".toRegex(), "").replace(",", ".")
            .toDoubleOrNull() ?: 0.0
        if (viewModel.shouldAnimateNextMetrics) {
            view.animateCurrencyChange(current, value, currency)
        } else {
            view.text = CurrencyUtils.formatPrice(value, currency)
        }
    }

    private fun updateMetricText(view: TextView, text: String, currency: String) {
        val value = text.replace("[^\\d.,]".toRegex(), "").replace(",", ".").toDoubleOrNull() ?: 0.0
        updateMetricText(view, value, currency)
    }

    /**
     * Подписка на список подписок: отображение, placeholder, анимация
     */
    private fun observeSubscriptions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.subscriptions.collect { list ->
                // Показываем только при первом появлении с анимацией
                if (!hasAnimatedList && list.isNotEmpty()) {
                    hasAnimatedList = true
                    adapter.submitListWithAnimation(list)
                } else {
                    adapter.submitList(list)
                }

                binding.subscriptionsList.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
                binding.placeholderGroup.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    /**
     * Обработка нажатий по метрикам:
     * – переключение периода
     * – переход к деталям по самой дорогой/дешёвой подписке
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setupMetricClicks() {
        setupAnimatedClick(binding.statsTitle) {
            viewModel.switchMetricsPeriod()
        }

        setupAnimatedClick(binding.highestSubs) {
            viewModel.metrics.value.highest?.let {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailsSubFragment(it.first.id)
                )
            }
        }

        setupAnimatedClick(binding.lowestSubs) {
            viewModel.metrics.value.lowest?.let {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailsSubFragment(it.first.id)
                )
            }
        }
    }

    /**
     * Обработка кликов по иконкам:
     * – настройки
     * – календарь
     * – добавить подписку
     * – сортировка
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setupButtonsClicks() {
        setupAnimatedClick(binding.settingsButton) {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }

        setupAnimatedClick(binding.fabCalendar) {
            findNavController().navigate(R.id.action_homeFragment_to_calendarFragment)
        }

        setupAnimatedClick(binding.fabAdd) {
            findNavController().navigate(R.id.action_homeFragment_to_addSubSearchFragment)
        }

        setupAnimatedClick(binding.sort) {
            SortBottomSheet(
                viewModel.sortOption.value,
                viewModel.sortDirection.value,
                onSortChanged = { option, direction ->
                    viewModel.setSort(option, direction)
                    viewModel.reapplySorting(adapter) {
                        binding.subscriptionsList.scrollToPosition(0)
                    }
                },
                onApply = {
                    //
                },
            ).show(parentFragmentManager, "SortBottomSheet")
        }

    }

    /**
     * Подписка на изменение сортировки и обновление кнопки сортировки
     */
    private fun observeSortState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.combinedSortState.collectLatest {
                updateFilterButton()
            }
        }
    }


    private fun updateFilterButton() {
        val label = when (viewModel.sortOption.value) {
            SortOption.DATE -> "Date"
            SortOption.NAME -> "Name"
            SortOption.PRICE -> "Price"
        }
        val icon = if (viewModel.sortDirection.value == SortDirection.ASC)
            R.drawable.ic_sort_up else R.drawable.ic_sort_down

        binding.sort.text = label
        binding.sort.setIconResource(icon)
    }

    /**
     * Добавление анимации нажатия и вибрации на любую вьюху
     */
    private fun setupAnimatedClick(view: View, onClick: () -> Unit) {
        val scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down)
        val scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)

        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.startAnimation(scaleDown)
                    vibrateLight()
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.startAnimation(scaleUp)
            }
            false
        }
        view.setOnClickListener { onClick() }
    }

    /**
     * Анимированное изменение числового значения с форматированием валюты
     */
    private fun TextView.animateCurrencyChange(
        start: Double,
        end: Double,
        currencyCode: String,
        duration: Long = 500L
    ) {
        if (start == end) {
            text = CurrencyUtils.formatPrice(end, currencyCode)
            return
        }

        ValueAnimator.ofFloat(start.toFloat(), end.toFloat()).apply {
            this.duration = duration
            addUpdateListener {
                val value = (it.animatedValue as Float).toDouble()
                text = CurrencyUtils.formatPrice(value, currencyCode)
            }
            start()
        }
    }

    private fun vibrateLight() {
        val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val manager = requireContext().getSystemService(android.os.VibratorManager::class.java)
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            requireContext().getSystemService(Vibrator::class.java)
        }

        vibrator?.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                it.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(30)
            }
        }
    }
}
