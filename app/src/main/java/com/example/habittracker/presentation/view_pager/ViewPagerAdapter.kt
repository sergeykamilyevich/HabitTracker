package com.example.habittracker.presentation.view_pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.habittracker.presentation.ui.HabitListFragment

class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HabitListFragment.newInstanceAllHabits()
            1 -> HabitListFragment.newInstanceGoodHabits()
            2 -> HabitListFragment.newInstanceBadHabits()
            else -> throw RuntimeException("Adapter position is out of range")
        }
    }
}