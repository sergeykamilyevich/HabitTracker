package com.example.habittracker.feature_habits.presentation.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.core.domain.models.HabitDone
import com.example.habittracker.core.domain.models.HabitType
import com.example.habittracker.core.domain.models.Time
import com.example.habittracker.feature_habits.R
import com.example.habittracker.feature_habits.databinding.FragmentHabitListBinding
import com.example.habittracker.feature_habits.presentation.models.HabitTypeApp
import com.example.habittracker.feature_habits.presentation.recycler.HabitListAdapter
import com.example.habittracker.feature_habits.presentation.view_models.MainViewModel
import com.example.habittracker.ui_kit.R.string
import javax.inject.Inject


class HabitListFragment : Fragment(), HasTitle {

    private var _binding: FragmentHabitListBinding? = null
    private val binding: FragmentHabitListBinding
        get() = _binding ?: throw RuntimeException("FragmentHabitListBinding is null")

    @Inject
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var time: Time

    private lateinit var habitListAdapter: HabitListAdapter

    private var listMode: HabitTypeApp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("99999", "HabitListFragment $this")
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).featureHabitsComponent.inject(this)
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
        setUpViewModel()
        setUpRecyclerView()
        setUpAdapterClickListeners()
        setUpSwipeListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpViewModel() {
        viewModel.getHabitList(HabitTypeApp.toHabitType(listMode))

        viewModel.habitList.observe(viewLifecycleOwner) {
            habitListAdapter.submitList(it)
        }
    }

    private fun setUpRecyclerView() {
        habitListAdapter = HabitListAdapter()
        binding.rvHabitList.adapter = habitListAdapter
    }

    private fun setUpAdapterClickListeners() {
        habitListAdapter.onButtonHabitDoneClickListener = {
            if (!viewModel.isHabitDoneButtonsBlocked()) {
                viewModel.addHabitDone(
                    HabitDone(
                        habitId = it.id,
                        date = time.currentUtcDateInSeconds(),
                        habitUid = it.uid
                    )
                )
            }
        }
        habitListAdapter.onHabitListClickListener = {
            launchHabitItemFragmentEditMode(it.id)
        }
    }

    private fun setUpSwipeListener() {
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

    private fun launchHabitItemFragmentEditMode(habitItemId: Int) {
        val destinationId = R.id.habitItemFragment
        val args = HabitItemFragment.createArgs(habitItemId)
        findNavController().navigate(destinationId, args)
    }

    override fun getTitleResId(): Int = string.habit_list

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