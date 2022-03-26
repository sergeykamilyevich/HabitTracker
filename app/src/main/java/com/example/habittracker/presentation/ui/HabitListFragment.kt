package com.example.habittracker.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitListBinding
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitPriority
import com.example.habittracker.domain.HabitType
import com.example.habittracker.presentation.color.ColorPicker
import com.example.habittracker.presentation.recycler.HabitListAdapter
import com.example.habittracker.presentation.view_models.HabitListViewModel
import kotlin.random.Random


class HabitListFragment : Fragment(), HasTitle {

    private var _binding: FragmentHabitListBinding? = null
    private val binding: FragmentHabitListBinding
        get() = _binding ?: throw RuntimeException("FragmentHabitListBinding is null")

    private val viewModel: HabitListViewModel by viewModels()
    private lateinit var habitListAdapter: HabitListAdapter
    private val colorPicker = ColorPicker()
    private val colors = colorPicker.getColors()

    private var listMode: String = ALL_HABITS_MODE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    private fun parseArguments() {
        arguments?.let {
            listMode = it.getString(LIST_MODE)
                ?: ALL_HABITS_MODE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        createRandomHabits()
        setupViewModel()
        setupRecyclerView()
        setupAdapterClickListener()
        setupAddButtonClickListener()
        setupSwipeListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewModel() {
        when (listMode) {
            ALL_HABITS_MODE -> viewModel.habitList.observe(viewLifecycleOwner) {
                habitListAdapter.submitList(it)
            }
            GOOD_HABITS_MODE -> viewModel.habitGoodList.observe(viewLifecycleOwner) {
                habitListAdapter.submitList(it)
            }
            BAD_HABITS_MODE -> viewModel.habitBadList.observe(viewLifecycleOwner) {
                habitListAdapter.submitList(it)
            }
        }
    }

    private fun setupRecyclerView() {
        habitListAdapter = HabitListAdapter()
        binding.rvHabitList.adapter = habitListAdapter
    }

    private fun setupAdapterClickListener() {
        habitListAdapter.onHabitListClickListener = {
            launchHabitItemActivityEditMode(it.id)
        }
    }

    private fun setupAddButtonClickListener() {
        binding.btnAddWord.setOnClickListener {
            launchHabitItemActivityAddMode()
        }
    }

    private fun setupSwipeListener() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val habitItem = habitListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteHabitItem(habitItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvHabitList)
    }

    private fun createRandomHabits() {
        for (i in 1..15) {
            viewModel.addHabitItem(
                HabitItem(
                    "Name $i",
                    "This habit is very important for my self-development",
                    HabitPriority.NORMAL,
                    HabitType.GOOD,
                    colors[Random.nextInt(16)],
                    Random.nextInt(10) + 1,
                    Random.nextInt(30) + 1
                )
            )
        }
    }

    private fun launchHabitItemActivityAddMode() {
        launchDestination(R.id.habitItemFragment, HabitItemFragment.createArgs(HabitItem.UNDEFINED_ID))
    }

    private fun launchHabitItemActivityEditMode(habitItemId: Int) {
        launchDestination(R.id.habitItemFragment, HabitItemFragment.createArgs(habitItemId))

    }

    private fun launchDestination(destinationId: Int, args: Bundle) {
        findNavController().navigate(destinationId, args)
    }

    override fun getTitleResId(): Int = R.string.habit_list


    companion object {
        private const val LIST_MODE = "list mode"
        private const val ALL_HABITS_MODE = "all habits"
        private const val GOOD_HABITS_MODE = "good habits"
        private const val BAD_HABITS_MODE = "bad habits"

        fun newInstanceAllHabits() = HabitListFragment().apply {
            arguments = Bundle().apply {
                putString(LIST_MODE, ALL_HABITS_MODE)
            }
        }

        fun newInstanceGoodHabits() = HabitListFragment().apply {
            arguments = Bundle().apply {
                putString(LIST_MODE, GOOD_HABITS_MODE)
            }
        }

        fun newInstanceBadHabits() = HabitListFragment().apply {
            arguments = Bundle().apply {
                putString(LIST_MODE, BAD_HABITS_MODE)
            }
        }
    }
}