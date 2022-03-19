package com.example.habittracker.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.habittracker.databinding.ItemHabitBinding
import com.example.habittracker.domain.HabitItem

class HabitListAdapter : ListAdapter<HabitItem, HabitItemViewHolder>(HabitItemDiffCallback()) {

    var onHabitListClickListener: ((HabitItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitItemViewHolder {
        val binding = ItemHabitBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HabitItemViewHolder(binding, onHabitListClickListener)
    }

    override fun onBindViewHolder(viewHolder: HabitItemViewHolder, position: Int) {
        val habitItem = getItem(position)
        viewHolder.bindItem(habitItem)
    }
}