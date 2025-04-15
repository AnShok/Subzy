package com.anshok.subzy.presentation.home

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
import com.anshok.subzy.util.safeDelayedAction
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: MySubViewModel by viewModel()

    private lateinit var adapter: MySubAdapter

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

    /**
     * Первичная анимация при открытии экрана + авто-прокрутка текста у метрик
     */
    private fun setupInitialAnimations() {
        binding.root.alpha = 0f
        binding.root.animate().alpha(1f).setDuration(250).start()

        binding.activeSubsSum.safeDelayedAction(2000) {
            binding.activeSubsSum.isSelected = true
            binding.highestSubsAmount.isSelected = true
            binding.lowestSubsAmount.isSelected = true
        }
    }

    /**
     * Настройка RecyclerView: layout, адаптер и переход к деталям по клику
     */
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
     * Подписка на изменения метрик.
     * Обновляет UI с защитой от глюков TextView при одинаковом тексте.
     */
    private fun observeMetrics() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.metrics.collectLatest { metrics ->

                binding.statsTitle.text = metrics.period.displayText()

                safeSetText(binding.activeSubsSum, metrics.totalFormatted)
                safeSetText(binding.highestSubsAmount, metrics.highestFormatted)
                safeSetText(binding.lowestSubsAmount, metrics.lowestFormatted)

            }
        }
    }

    /**
     * Безопасная установка текста в TextView.
     * Принудительно обновляет UI, даже если текст визуально не изменился.
     */
    private fun safeSetText(textView: TextView, newText: String?) {
        val current = textView.text.toString()
        if (current != (newText ?: "--")) {
            textView.text = ""
            textView.postDelayed({
                textView.text = newText ?: "--"
            }, 10)
        }
    }


    /**
     * Подписка на список подписок.
     * Обновляет адаптер и отображение плейсхолдера.
     */
    private fun observeSubscriptions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.subscriptions.collect { list ->
                adapter.submitListWithCallback(list) {
                    // Прокрутка наверх после вставки
                    binding.subscriptionsList.scrollToPosition(0)
                }

                binding.subscriptionsList.visibility =
                    if (list.isEmpty()) View.GONE else View.VISIBLE
                binding.placeholderGroup.visibility =
                    if (list.isEmpty()) View.VISIBLE else View.GONE
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
            viewModel.metrics.value.highestSub?.let { sub ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailsSubFragment(sub.id)
                )
            }
        }

        setupAnimatedClick(binding.lowestSubs) {
            viewModel.metrics.value.lowestSub?.let { sub ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailsSubFragment(sub.id)
                )
            }
        }
    }


    /**
     * Обработка нажатий на кнопки и FAB-элементы:
     * - переход в настройки, календарь, добавление и сортировку
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
     * Подписка на изменение состояния сортировки.
     * Обновляет внешний вид кнопки сортировки.
     */
    private fun observeSortState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.combinedSortState.collectLatest {
                updateFilterButton()
            }
        }
    }

    /**
     * Отображает текущую сортировку и направление в UI
     */
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
     * Универсальный обработчик анимации нажатия + вибрация
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
