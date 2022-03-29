package com.example.habittracker.presentation.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.habittracker.domain.HabitItem

class HabitItemDiffCallback : DiffUtil.ItemCallback<HabitItem>() {

    override fun areItemsTheSame(oldItem: HabitItem, newItem: HabitItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HabitItem, newItem: HabitItem): Boolean {
        return oldItem == newItem
    }

}