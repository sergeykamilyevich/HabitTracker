package com.example.habittracker.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitListPagerBinding
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitListOrderBy
import com.example.habittracker.presentation.view_models.HabitListViewModel
import com.example.habittracker.presentation.view_pager.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class HabitListPagerFragment : Fragment(), HasTitle {

    private var _binding: FragmentHabitListPagerBinding? = null
    private val binding: FragmentHabitListPagerBinding
        get() = _binding ?: throw RuntimeException("FragmentHabitListPagerBinding is null")

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private val viewModel: HabitListViewModel by activityViewModels()

    private lateinit var buttons: ArrayList<Button>

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
        setupBottomSheet()
        setupAddButton()
    }

    private fun setupViewPager() {
        val tabNames: Array<String> = arrayOf(
            getString(R.string.all_habits),
            getString(R.string.good_habits),
            getString(R.string.bad_habits)
        )
        viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager2Fragment.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayoutFragment, binding.viewPager2Fragment) { tab, position ->
            tab.text = tabNames[position]
        }.attach()
    }

    private fun setupBottomSheet() {
        setupBottomSheetButtons()
        setupBottomSheetTied()
    }

    private fun setupBottomSheetButtons() {
        buttons = arrayListOf(
            binding.btnNameAsc,
            binding.btnNameDesc,
            binding.btnCreationAsc,
            binding.btnCreationDesc
        )
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                setCurrentButtonIsSelected(index)
                setHabitListOrderByFromSelectedButton(button)
            }
        }
        if (viewModel.habitListFilter.value == null) {
            setDefaultButtonsState()
        } else {
            setSelectedButtonFromHabitListOrderBy(
                viewModel.habitListFilter.value?.orderBy ?: HabitListOrderBy.NAME_ASC
            )
        }
    }

    private fun setupBottomSheetTied() {
        binding.tiedSearch.addTextChangedListener {
            viewModel.updateFilter(it)
        }
    }

    private fun setHabitListOrderByFromSelectedButton(button: Button) {
        val habitListOrderBy = with(binding) {
            when (button.id) {
                btnNameAsc.id -> HabitListOrderBy.NAME_ASC
                btnNameDesc.id -> HabitListOrderBy.NAME_DESC
                btnCreationAsc.id -> HabitListOrderBy.TIME_CREATION_ASC
                btnCreationDesc.id -> HabitListOrderBy.TIME_CREATION_DESC
                else -> throw RuntimeException("Unknown button: ${button.id}")
            }
        }
        viewModel.updateHabitListOrderBy(habitListOrderBy)

    }

    private fun setSelectedButtonFromHabitListOrderBy(habitListOrderBy: HabitListOrderBy) {
        with(binding) {
            when (habitListOrderBy) {
                HabitListOrderBy.NAME_ASC -> btnNameAsc.isSelected = true
                HabitListOrderBy.NAME_DESC -> btnNameDesc.isSelected = true
                HabitListOrderBy.TIME_CREATION_ASC -> btnCreationAsc.isSelected = true
                HabitListOrderBy.TIME_CREATION_DESC -> btnCreationDesc.isSelected = true
            }
        }
    }

    private fun setCurrentButtonIsSelected(selectedButtonIndex: Int) {
        buttons.forEachIndexed { index, button ->
            button.isSelected = (index == selectedButtonIndex)
        }
    }

    private fun setDefaultButtonsState() {
        setCurrentButtonIsSelected(0)
        setHabitListOrderByFromSelectedButton(buttons[0])
    }

    private fun setupAddButton() {
        binding.btnAddWord.setOnClickListener {
            launchHabitItemActivityAddMode()
        }
    }

    private fun launchHabitItemActivityAddMode() {
        val destinationId = R.id.habitItemFragment
        val args = HabitItemFragment.createArgs(HabitItem.UNDEFINED_ID)
        findNavController().navigate(destinationId, args)
    }

    override fun getTitleResId(): Int = R.string.habit_list
}