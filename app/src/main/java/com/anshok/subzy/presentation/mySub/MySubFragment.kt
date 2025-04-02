package com.anshok.subzy.presentation.mySub

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentMySubBinding
import com.anshok.subzy.presentation.mySub.adapter.SubscriptionPagerAdapter
import com.anshok.subzy.presentation.mySub.viewmodel.MySubViewModel
import com.anshok.subzy.util.CurrencyUtils
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MySubFragment : Fragment() {

    private val binding: FragmentMySubBinding by viewBinding(CreateMethod.INFLATE)
    private val tabTitles = arrayListOf("Your subscriptions", "Upcoming bills")
    private val viewModel: MySubViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setUpTabLayoutWithViewPager()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeMetrics()
        setupMetricClicks()
    }

    private fun observeMetrics() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.metrics.collectLatest { (total, highest, lowest) ->
                val defaultCurrency = viewModel.getDefaultCurrencyCode()

                binding.activeSubsCount.text = total
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

        setupAnimatedClick(binding.activeSubs) {
            val total = viewModel.metrics.value.first
            Toast.makeText(
                requireContext(),
                "Сумма ваших подписок в месяц составляет $total",
                Toast.LENGTH_SHORT
            ).show()
        }

        setupAnimatedClick(binding.highestSubs) {
            viewModel.metrics.value.second?.let {
                val action = MySubFragmentDirections.actionMySubFragmentToDetailsSubFragment(it.first.id)
                findNavController().navigate(action)
            }
        }

        setupAnimatedClick(binding.lowestSubs) {
            viewModel.metrics.value.third?.let {
                val action = MySubFragmentDirections.actionMySubFragmentToDetailsSubFragment(it.first.id)
                findNavController().navigate(action)
            }
        }
    }

    private fun setUpTabLayoutWithViewPager() {
        binding.viewPager.adapter = SubscriptionPagerAdapter(this)
        TabLayoutMediator(binding.tabLayoutSubscription, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        for (i in 0..1) {
            val textView = LayoutInflater.from(requireContext())
                .inflate(R.layout.tab_title, null) as TextView
            binding.tabLayoutSubscription.getTabAt(i)?.customView = textView
        }
    }

    private fun vibrateLight() {
        val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val manager = requireContext().getSystemService(android.os.VibratorManager::class.java)
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            requireContext().getSystemService(android.content.Context.VIBRATOR_SERVICE) as android.os.Vibrator
        }

        vibrator?.let {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                it.vibrate(
                    android.os.VibrationEffect.createOneShot(
                        30,
                        android.os.VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(30)
            }
        }
    }
}
