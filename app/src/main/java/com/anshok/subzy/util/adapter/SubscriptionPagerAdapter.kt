package com.anshok.subzy.util.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.anshok.subzy.presentation.ui.UpcomingBillsFragment
import com.anshok.subzy.presentation.ui.YourSubscriptionsFragment

class SubscriptionPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2 // Две вкладки
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> YourSubscriptionsFragment() // Вкладка 1
            1 -> UpcomingBillsFragment()     // Вкладка 2
            else -> YourSubscriptionsFragment()
        }
    }
}