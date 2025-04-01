package com.anshok.subzy.presentation.mySub.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.anshok.subzy.presentation.mySub.MySubFragment
import com.anshok.subzy.presentation.mySub.RvItemSubFragment
import com.anshok.subzy.presentation.mySub.RvUpcomingBillsFragment

class SubscriptionPagerAdapter(fragmentActivity: MySubFragment) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2 // Две вкладки

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RvItemSubFragment()
            1 -> RvUpcomingBillsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}