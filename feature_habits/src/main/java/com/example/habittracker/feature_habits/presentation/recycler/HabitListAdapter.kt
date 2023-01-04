package com.example.habittracker.feature_habits.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.feature_habits.databinding.ItemHabitBinding

class HabitListAdapter : ListAdapter<Habit, HabitItemViewHolder>(HabitItemDiffCallback()) {

    var onHabitListClickListener: ((Habit) -> Unit)? = null
    var onButtonHabitDoneClickListener: ((Habit) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitItemViewHolder {
        val binding = ItemHabitBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HabitItemViewHolder(
            binding,
            onHabitListClickListener,
            onButtonHabitDoneClickListener
        )
    }

    override fun onBindViewHolder(viewHolder: HabitItemViewHolder, position: Int) {
        val habitItem = getItem(position)
        viewHolder.bindItem(habitItem)
    }
}