package com.example.habittracker.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.habittracker.BuildConfig
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentAboutBinding
import com.example.habittracker.databinding.FragmentHabitListPagerBinding
import com.example.habittracker.presentation.view_pager.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class HabitListPagerFragment : Fragment(), HasTitle {

    private var _binding: FragmentHabitListPagerBinding? = null
    private val binding: FragmentHabitListPagerBinding
        get() = _binding ?: throw RuntimeException("FragmentHabitListPagerBinding is null")

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitListPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {
        val tabNames: Array<String> = arrayOf(
            getString(R.string.all_habits),
            getString(R.string.good_habits),
            getString(R.string.bad_habits)
        )
        viewPagerAdapter = ViewPagerAdapter(activity as AppCompatActivity)
        binding.viewPager2Fragment.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayoutFragment, binding.viewPager2Fragment) { tab, position ->
            tab.text = tabNames[position]
        }.attach()
    }
    override fun getTitleResId(): Int = R.string.about


}