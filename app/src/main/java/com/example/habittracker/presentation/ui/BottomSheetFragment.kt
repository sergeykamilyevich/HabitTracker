package com.example.habittracker.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.habittracker.databinding.FragmentBottomSheetBinding
import com.example.habittracker.domain.models.HabitListOrderBy
import com.example.habittracker.presentation.view_models.MainViewModel
import javax.inject.Inject


class BottomSheetFragment : Fragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding: FragmentBottomSheetBinding
        get() = _binding ?: throw RuntimeException("FragmentBottomSheetBinding is null")

    private lateinit var buttons: ArrayList<Button>

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainActivityComponent.inject(this)
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
        if (viewModel.habitListFilter.value == null) {
            setUpDefaultButtonsState()
        } else {
            setUpSelectedButtonFromHabitListOrderBy(
                viewModel.habitListFilter.value?.orderBy ?: HabitListOrderBy.NAME_ASC
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
                btnPriorityAsc.id -> HabitListOrderBy.PRIORITY_ASC
                btnPriorityDesc.id -> HabitListOrderBy.PRIORITY_DESC
                else -> throw RuntimeException("Unknown button: ${button.id}")
            }
        }
        viewModel.updateHabitListOrderBy(habitListOrderBy)
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