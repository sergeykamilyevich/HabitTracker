package com.example.habittracker.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitListBinding
import com.example.habittracker.domain.color.ColorPicker
import com.example.habittracker.domain.entities.*
import com.example.habittracker.presentation.recycler.HabitListAdapter
import com.example.habittracker.presentation.view_models.HabitListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random


class HabitListFragment : Fragment(), HasTitle {

    private var _binding: FragmentHabitListBinding? = null
    private val binding: FragmentHabitListBinding
        get() = _binding ?: throw RuntimeException("FragmentHabitListBinding is null")

    private val viewModel: HabitListViewModel by activityViewModels()
    private lateinit var habitListAdapter: HabitListAdapter
    private val colorPicker = ColorPicker()
    private val colors = colorPicker.getColors()
    private val habitTime = HabitTime()

    private var listMode: HabitType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    private fun parseArguments() {
        arguments?.let {
            listMode = it.getParcelable(LIST_MODE)
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
        setupAdapterClickListeners()
        setupSwipeListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewModel() {
        viewModel.getHabitList(listMode)

        viewModel.habitList.observe(viewLifecycleOwner) {
            habitListAdapter.submitList(it)
        }
        viewModel.habitListFilter.observe(viewLifecycleOwner) {
            viewModel.getHabitList(listMode)
        }
    }

    private fun setupRecyclerView() {
        habitListAdapter = HabitListAdapter()
        binding.rvHabitList.adapter = habitListAdapter
    }

    private fun setupAdapterClickListeners() {
        habitListAdapter.onButtonHabitDoneClickListener = {
            viewModel.addHabitDone(
                HabitDone(
                    habitId = it.id,
                    date = habitTime.getCurrentUtcDateInInt()
                )
            )
            Snackbar.make(
                binding.fragmentHabitList,
                "You marked habit '${it.name}' as Done",
                Snackbar.LENGTH_LONG
            )
                .setAction("Undo") {
                    viewModel.habitDoneIdAdded?.let { habitDoneId ->
                        viewModel.deleteHabitDone(habitDoneId)
                    }
                }
                .setDuration(5000)
                .show()
        }
        habitListAdapter.onHabitListClickListener = {
            launchHabitItemActivityEditMode(it.id)
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
                    Random.nextInt(30) + 1,
                    date = HabitTime().getCurrentUtcDateInInt()
                )
            )
        }
    }

    private fun launchHabitItemActivityEditMode(habitItemId: Int) {
        val destinationId = R.id.habitItemFragment
        val args = HabitItemFragment.createArgs(habitItemId)
        findNavController().navigate(destinationId, args)
    }

    override fun getTitleResId(): Int = R.string.habit_list

    companion object {
        private const val LIST_MODE = "list mode"

        fun newInstance(habitTypeFilter: HabitType?) = HabitListFragment().apply {
            arguments = Bundle().apply {
                putParcelable(LIST_MODE, habitTypeFilter)
            }
        }
    }
}