package com.example.habittracker.presentation.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.habittracker.domain.models.Habit

class HabitItemDiffCallback : DiffUtil.ItemCallback<Habit>() {

    override fun areItemsTheSame(old: Habit, aNew: Habit): Boolean {
        return old.id == aNew.id
    }

    override fun areContentsTheSame(old: Habit, aNew: Habit): Boolean {
        return old == aNew
    }

}