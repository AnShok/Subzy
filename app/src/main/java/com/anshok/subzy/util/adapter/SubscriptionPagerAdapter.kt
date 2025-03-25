package com.anshok.subzy.util.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.anshok.subzy.presentation.home.HomeFragment
import com.anshok.subzy.presentation.home.HomeRvSubFragment
import com.anshok.subzy.presentation.home.HomeRvUpcomingBillsFragment

class SubscriptionPagerAdapter(fragmentActivity: HomeFragment) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2 // Две вкладки
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeRvSubFragment() // Вкладка 1
            1 -> HomeRvUpcomingBillsFragment()     // Вкладка 2
            else -> HomeRvSubFragment()
        }
    }
}