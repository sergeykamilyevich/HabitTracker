package com.example.habittracker.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.habittracker.R
import com.example.habittracker.databinding.ActivityHabitListBinding
import com.example.habittracker.presentation.view_pager.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class HabitListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHabitListBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewPager()

    }

    private fun setupViewPager() {
        val tabNames: Array<String> = arrayOf(
            getString(R.string.all_habits),
            getString(R.string.good_habits),
            getString(R.string.bad_habits)
        )
        viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager2.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = tabNames[position]
        }.attach()
    }
}
