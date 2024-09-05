package com.anshok.subzy.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anshok.subzy.R
import com.anshok.subzy.databinding.FragmentHomeBinding
import com.anshok.subzy.util.adapter.SubscriptionPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //// Установка адаптера для ViewPager2
        //val adapter = SubscriptionPagerAdapter(requireActivity())
        //binding.viewPager.adapter = adapter
//
        //// Привязка TabLayout к ViewPager2
        //TabLayoutMediator(binding.tabLayoutSubscription, binding.viewPager) { tab, position ->
        //    when (position) {
        //        0 -> tab.text = "Your subscriptions"
        //        1 -> tab.text = "Upcoming bills"
        //    }
        //}.attach()
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
}