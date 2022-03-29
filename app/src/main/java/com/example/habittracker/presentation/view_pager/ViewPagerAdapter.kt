package com.example.habittracker.presentation.view_pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.habittracker.domain.HabitType
import com.example.habittracker.presentation.ui.HabitListFragment

class ViewPagerAdapter(parentFragment: Fragment) : FragmentStateAdapter(parentFragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HabitListFragment.newInstance(null)
            1 -> HabitListFragment.newInstance(HabitType.GOOD)
            2 -> HabitListFragment.newInstance(HabitType.BAD)
            else -> throw RuntimeException("Adapter position is out of range")
        }
    }
}