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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentHomeBinding
import com.anshok.subzy.presentation.home.adapter.SubscriptionsAdapter
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
    private lateinit var adapter: SubscriptionsAdapter
    private var hasAnimatedList = false
    private var hasAnimatedMetrics = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInitialAnimations()
        setupRecyclerView()
        observeMetrics()
        observeSubscriptions()
        setupMetricClicks()
        setupButtonsClicks()
        observeRefreshAfterSave()

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
        adapter = SubscriptionsAdapter { subscription ->
            val action =
                HomeFragmentDirections.actionHomeFragmentToDetailsSubFragment(subscription.id)
            findNavController().navigate(action)
        }

        binding.subscriptionsList.layoutManager = LinearLayoutManager(requireContext())
        binding.subscriptionsList.adapter = adapter
    }

    private fun observeMetrics() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.metrics.collectLatest { (total, highest, lowest) ->
                val defaultCurrency = viewModel.getDefaultCurrencyCode()

                binding.activeSubsCount.text =
                    if (highest == null && lowest == null) "--" else total
                binding.highestSubsAmount.text = highest?.let {
                    CurrencyUtils.formatPrice(it.second, defaultCurrency)
                } ?: "--"
                binding.lowestSubsAmount.text = lowest?.let {
                    CurrencyUtils.formatPrice(it.second, defaultCurrency)
                } ?: "--"

                if (!hasAnimatedMetrics) {
                    hasAnimatedMetrics = true
                    binding.metricsCard.animateAppear()
                }
            }
        }
    }

    private fun observeSubscriptions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.subscriptions.collect { list ->
                adapter.submitList(list)

                val isEmpty = list.isEmpty()
                binding.subscriptionsList.visibility = if (isEmpty) View.GONE else View.VISIBLE
                binding.placeholderGroup.visibility = if (isEmpty) View.VISIBLE else View.GONE

                if (!hasAnimatedList && list.isNotEmpty()) {
                    hasAnimatedList = true
                    binding.subscriptionsList.fadeInWithTranslation()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupMetricClicks() {
        val scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down)
        val scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)

        fun setupAnimatedClick(view: View, onClick: () -> Unit) {
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


        setupAnimatedClick(binding.highestSubs) {
            viewModel.metrics.value.second?.let {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailsSubFragment(it.first.id)
                findNavController().navigate(action)
            }
        }

        setupAnimatedClick(binding.lowestSubs) {
            viewModel.metrics.value.third?.let {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailsSubFragment(it.first.id)
                findNavController().navigate(action)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupButtonsClicks() {
        val scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down)
        val scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)

        fun setupAnimatedClick(view: View, onClick: () -> Unit) {
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

        setupAnimatedClick(binding.settingsButton) {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }

        setupAnimatedClick(binding.fabCalendar) {
            findNavController().navigate(R.id.action_homeFragment_to_calendarFragment)
        }

        setupAnimatedClick(binding.fabAdd) {
            findNavController().navigate(R.id.action_homeFragment_to_addSubSearchFragment)
        }
    }

    private fun observeRefreshAfterSave() {
        val savedStateHandle = findNavController().currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<Boolean>("refreshAfterSave")
            ?.observe(viewLifecycleOwner) { shouldRefresh ->
                if (shouldRefresh == true) {
                    viewModel.refreshSubscriptionsManually {}
                    savedStateHandle.remove<Boolean>("refreshAfterSave")
                }
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