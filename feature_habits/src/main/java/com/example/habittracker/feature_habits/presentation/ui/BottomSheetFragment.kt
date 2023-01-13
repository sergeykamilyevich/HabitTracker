package com.example.habittracker.feature_habits.presentation.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.habittracker.core_api.domain.models.HabitListOrderBy
import com.example.habittracker.feature_habit_filter_api.di.mediators.HabitFilterMediator
import com.example.habittracker.feature_habit_filter_api.presentation.view_models.FilterViewModel
import com.example.habittracker.feature_habits.databinding.FragmentBottomSheetBinding
import javax.inject.Inject

class BottomSheetFragment : Fragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding: FragmentBottomSheetBinding
        get() = _binding ?: throw RuntimeException("FragmentBottomSheetBinding is null")

    private lateinit var buttons: ArrayList<Button>

    @Inject
    lateinit var habitFilterMediator: HabitFilterMediator

    private val filterViewModel: FilterViewModel by lazy {
        habitFilterMediator.filterViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("99999", "BottomSheetFragment $this")
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).featureHabitsComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBottomSheet()
    }

    private fun setUpBottomSheet() {
        setUpBottomSheetButtons()
        setUpBottomSheetTied()
    }

    private fun setUpBottomSheetButtons() {
        buttons = arrayListOf(
            binding.btnNameAsc,
            binding.btnNameDesc,
            binding.btnCreationAsc,
            binding.btnCreationDesc,
            binding.btnPriorityAsc,
            binding.btnPriorityDesc
        )
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                setCurrentButtonIsSelected(index)
                setHabitListOrderByFromSelectedButton(button)
            }
        }
        if (filterViewModel.habitListFilter.value == null) {
            setUpDefaultButtonsState()
        } else {
            setUpSelectedButtonFromHabitListOrderBy(
                filterViewModel.habitListFilter.value?.orderBy ?: HabitListOrderBy.NAME_ASC
            )
        }
    }

    private fun setCurrentButtonIsSelected(selectedButtonIndex: Int) {
        buttons.forEachIndexed { index, button ->
            button.isSelected = (index == selectedButtonIndex)
        }
    }

    private fun setUpBottomSheetTied() {
        binding.tiedSearch.addTextChangedListener {
            filterViewModel.updateSearch(it)
        }
    }

    private fun setHabitListOrderByFromSelectedButton(button: Button) {
        val habitListOrderBy = with(binding) {
            when (button.id) {
                btnNameAsc.id -> HabitListOrderBy.NAME_ASC
                btnNameDesc.id -> HabitListOrderBy.NAME_DESC
                btnCreationAsc.id -> HabitListOrderBy.TIME_CREATION_ASC
                btnCreationDesc.id -> HabitListOrderBy.TIME_CREATION_DESC
                btnPriorityAsc.id -> HabitListOrderBy.PRIORITY_ASC
                btnPriorityDesc.id -> HabitListOrderBy.PRIORITY_DESC
                else -> throw RuntimeException("Unknown button: ${button.id}")
            }
        }
        filterViewModel.updateHabitListOrderBy(habitListOrderBy)
    }

    private fun setUpSelectedButtonFromHabitListOrderBy(habitListOrderBy: HabitListOrderBy) {
        with(binding) {
            when (habitListOrderBy) {
                HabitListOrderBy.NAME_ASC -> btnNameAsc.isSelected = true
                HabitListOrderBy.NAME_DESC -> btnNameDesc.isSelected = true
                HabitListOrderBy.TIME_CREATION_ASC -> btnCreationAsc.isSelected = true
                HabitListOrderBy.TIME_CREATION_DESC -> btnCreationDesc.isSelected = true
                HabitListOrderBy.PRIORITY_ASC -> btnPriorityAsc.isSelected = true
                HabitListOrderBy.PRIORITY_DESC -> btnPriorityDesc.isSelected = true
            }
        }
    }

    private fun setUpDefaultButtonsState() {
        setCurrentButtonIsSelected(DEFAULT_SELECTED_BUTTON_INDEX)
        setHabitListOrderByFromSelectedButton(buttons[DEFAULT_SELECTED_BUTTON_INDEX])
    }

    companion object {
        private const val DEFAULT_SELECTED_BUTTON_INDEX = 0
    }
}