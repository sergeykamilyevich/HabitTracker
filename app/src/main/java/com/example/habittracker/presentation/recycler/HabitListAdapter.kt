package com.example.habittracker.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.habittracker.databinding.ItemHabitBinding
import com.example.habittracker.domain.models.Habit

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