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
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentHomeBinding
import com.anshok.subzy.presentation.home.adapter.HomePagerAdapter
import com.anshok.subzy.presentation.home.viewmodel.MySubViewModel
import com.anshok.subzy.util.CurrencyUtils
import com.anshok.subzy.util.safeDelayedAction
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by viewBinding(CreateMethod.INFLATE)
    private val tabTitles = arrayListOf("Your subscriptions", "Upcoming bills")
    private val viewModel: MySubViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setUpTabLayoutWithViewPager()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupButtonsClicks()
        observeMetrics()
        setupMetricClicks()

        binding.activeSubsCount.safeDelayedAction(2000) {
            binding.activeSubsCount.isSelected = true
            binding.highestSubsAmount.isSelected = true
            binding.lowestSubsAmount.isSelected = true
        }

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

    private fun setUpTabLayoutWithViewPager() {
        binding.viewPager.adapter = HomePagerAdapter(this)
        TabLayoutMediator(binding.tabLayoutSubscription, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        for (i in 0..1) {
            val textView = LayoutInflater.from(requireContext())
                .inflate(R.layout.tab_title, null) as TextView
            binding.tabLayoutSubscription.getTabAt(i)?.customView = textView
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
