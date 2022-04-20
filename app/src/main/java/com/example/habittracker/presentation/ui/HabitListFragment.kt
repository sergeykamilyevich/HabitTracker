package com.example.habittracker.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitListBinding
import com.example.habittracker.domain.models.*
import com.example.habittracker.presentation.color.ColorPicker
import com.example.habittracker.presentation.models.HabitTypeApp
import com.example.habittracker.presentation.recycler.HabitListAdapter
import com.example.habittracker.presentation.view_models.HabitListViewModel
import javax.inject.Inject
import kotlin.random.Random


class HabitListFragment : Fragment(), HasTitle {

    private var _binding: FragmentHabitListBinding? = null
    private val binding: FragmentHabitListBinding
        get() = _binding ?: throw RuntimeException("FragmentHabitListBinding is null")

    @Inject
    lateinit var viewModel: HabitListViewModel

    @Inject
    lateinit var colorPicker: ColorPicker
    private val colors by lazy { colorPicker.getColors() }

    @Inject
    lateinit var time: Time

    private lateinit var habitListAdapter: HabitListAdapter

    private var listMode: HabitTypeApp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainActivityComponent.inject(this)

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
        viewModel.getHabitList(HabitTypeApp.toHabitType(listMode))

        viewModel.habitList.observe(viewLifecycleOwner) {
            habitListAdapter.submitList(it)
        }
        viewModel.fetchHabits()
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
                    date = time.getCurrentUtcDateInInt()
                )
            )
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
                    date = time.getCurrentUtcDateInInt(),
                    done = 0
                )
            )
        }
        R.string.low_priority
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
                val habitTypeAppFilter =
                    if (habitTypeFilter != null) HabitTypeApp.fromHabitType(habitTypeFilter) else null
                putParcelable(LIST_MODE, habitTypeAppFilter)
            }
        }
    }
}