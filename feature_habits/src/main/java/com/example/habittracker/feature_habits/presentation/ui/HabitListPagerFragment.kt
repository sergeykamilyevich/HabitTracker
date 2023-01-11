package com.example.habittracker.feature_habits.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.feature_habits.R
import com.example.habittracker.feature_habits.databinding.FragmentHabitListPagerBinding
import com.example.habittracker.feature_habits.presentation.view_pager.ViewPagerAdapter
import com.example.habittracker.ui_kit.R.string
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
        Log.d("99999", "HabitListPagerFragment $this")
        _binding = FragmentHabitListPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()
        setUpAddButton()
    }

    private fun setUpViewPager() {
        val tabNames: Array<String> = arrayOf(
            getString(string.all_habits),
            getString(string.good_habits),
            getString(string.bad_habits)
        )
        viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager2Fragment.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayoutFragment, binding.viewPager2Fragment) { tab, position ->
            tab.text = tabNames[position]
        }.attach()
    }

    private fun setUpAddButton() {
        binding.btnAddHabit.setOnClickListener {
            launchHabitItemFragmentAddMode()
        }
    }

    private fun launchHabitItemFragmentAddMode() {
        val destinationId = R.id.habitItemFragment
        val args = HabitItemFragment.createArgs(Habit.UNDEFINED_ID)
        findNavController().navigate(destinationId, args)
    }

    override fun getTitleResId(): Int = string.habit_list
}