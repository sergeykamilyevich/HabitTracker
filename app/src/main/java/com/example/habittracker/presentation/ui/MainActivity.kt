package com.example.habittracker.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.databinding.ActivityMainBinding
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitPriority
import com.example.habittracker.domain.HabitType
import com.example.habittracker.presentation.color.ColorPicker
import com.example.habittracker.presentation.recycler.HabitItemViewHolder
import com.example.habittracker.presentation.recycler.HabitListAdapter
import com.example.habittracker.presentation.view_models.MainViewModel
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var habitListAdapter: HabitListAdapter
    private val colorPicker = ColorPicker()
    private val colors = colorPicker.getColors()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
        setupAdapterClickListener()
        setupAddButtonClickListener()
        setupSwipeListener()
        createRandomHabits()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.habitList.observe(this) {
            habitListAdapter.submitList(it)
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
        val intent = HabitItemActivity.newIntentAddMode(this)
        startActivity(intent)
    }

    private fun launchHabitItemActivityEditMode(habitItemId: Int) {
        val intent = HabitItemActivity.newIntentEditMode(this, habitItemId)
        startActivity(intent)
    }
}