package com.anshok.subzy.util.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.anshok.subzy.presentation.ui.HomeFragment
import com.anshok.subzy.presentation.ui.UpcomingBillsFragment
import com.anshok.subzy.presentation.ui.HomeRvYourSubFragment

class SubscriptionPagerAdapter(fragmentActivity: HomeFragment) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2 // Две вкладки
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeRvYourSubFragment() // Вкладка 1
            1 -> UpcomingBillsFragment()     // Вкладка 2
            else -> HomeRvYourSubFragment()
        }
    }
}