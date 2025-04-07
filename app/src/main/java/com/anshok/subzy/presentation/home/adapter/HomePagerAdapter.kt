package com.anshok.subzy.presentation.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.anshok.subzy.presentation.home.ActiveSubsFragment
import com.anshok.subzy.presentation.home.UpcomingBillsFragment

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ActiveSubsFragment()
            1 -> UpcomingBillsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
