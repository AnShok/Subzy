package com.anshok.subzy.presentation.ui

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentHomeBinding
import com.anshok.subzy.util.adapter.SubscriptionPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.zigis.segmentedarcview.custom.ArcSegment
import com.zigis.segmentedarcview.custom.BlinkAnimationSettings

class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by viewBinding(CreateMethod.INFLATE)
    private val tabTitles = arrayListOf("Your subscriptions", "Upcoming bills")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Возвращаем root view, как обычно
        setUpTabLayoutWithViewPager()
        setupDottedCircles()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupArcView(
            63f,  // progressPercentage - Процент заполения,
            260f    // maxSweepAngle - Максимальный угол секции
        )

        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
    }

    private fun setUpTabLayoutWithViewPager() {
        binding.viewPager.adapter = SubscriptionPagerAdapter(this)
        TabLayoutMediator(binding.tabLayoutSubscription, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        for (i in 0..2) {
            val textView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_title, null)
                    as TextView
            binding.tabLayoutSubscription.getTabAt(i)?.customView = textView
        }
    }

    private fun setupArcView(progressPercentage: Float, maxSweepAngle: Float) {
        // Задний фон
        val arcViewBackground = binding.progressViewBackground
        arcViewBackground.segments = listOf(
            ArcSegment(
                ContextCompat.getColor(requireContext(), R.color.Gray_62),
                ContextCompat.getColor(requireContext(), R.color.Gray_62)
            )
        )


        val arcView = binding.progressView
        val animator = ValueAnimator.ofFloat(0f, progressPercentage)
        animator.duration = 1500
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            arcView.segments = listOf(
                ArcSegment(
                    ContextCompat.getColor(requireContext(), R.color.Primary_10),
                    ContextCompat.getColor(requireContext(), R.color.Accent_P_100),
                    sweepAngle = maxSweepAngle * (animatedValue / 100f) //рассчет для секции
                )
            )
        }
        animator.start()

        arcView.blinkAnimationSettings = BlinkAnimationSettings(
            minAlpha = 0.4F,
            maxAlpha = 1F,
            duration = 2000L
        )
    }

    // Отдельный метод для настройки всех кругов
    private fun setupDottedCircles() {
        setupDotInside()
        setupDotOutside()
        setupDotOutsideSecond()
    }

    // Настройка внутреннего круга с заданными углами
    private fun setupDotInside() {
        val dotInside = binding.dotInside
        dotInside.setCircleParams(
            numberOfDots = 32,
            dotRadius = 5f,
            circleRadius = 315f,
            dotColor = ContextCompat.getColor(requireContext(), R.color.Gray_62),
            startAngle = 140.0,  // Угол начала
            endAngle = 408.0     // Угол конца
        )
    }

    // Настройка внешнего круга с заданными углами
    private fun setupDotOutside() {
        val dotOutside = binding.dotOutside
        dotOutside.setCircleParams(
            numberOfDots = 92,
            dotRadius = 5f,
            circleRadius = 450f,
            dotColor = ContextCompat.getColor(requireContext(), R.color.Gray_62),
            startAngle = 135.0,    // Угол начала
            endAngle = 408.0     // Угол конца
        )
    }

    // Настройка внешнего круга с заданными углами
    private fun setupDotOutsideSecond() {
        val dotOutside = binding.dotOutsideSecond
        dotOutside.setCircleParams(
            numberOfDots = 110,
            dotRadius = 5f,
            circleRadius = 520f,
            dotColor = ContextCompat.getColor(requireContext(), R.color.Gray_65),
            startAngle = 135.0,    // Угол начала
            endAngle = 408.0     // Угол конца
        )
    }
}