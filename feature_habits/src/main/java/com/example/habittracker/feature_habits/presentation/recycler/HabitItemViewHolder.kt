package com.example.habittracker.feature_habits.presentation.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.Time
import com.example.habittracker.feature_habits.databinding.ItemHabitBinding
import com.example.habittracker.feature_habits.presentation.models.HabitPriorityApp
import com.example.habittracker.feature_habits.presentation.models.HabitTypeApp
import com.example.habittracker.ui_kit.R


class HabitItemViewHolder(
    private val binding: ItemHabitBinding,
    private val onHabitListClickListener: ((Habit) -> Unit)?,
    private val onButtonHabitDoneClickListener: ((Habit) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    private val habitTime = Time()

    fun bindItem(habit: Habit) {
        with(binding) {
            tvDescription.text = habit.description
            tvName.text = habit.name
            tvDate.text = habitTime.utcDateToString(habit.date)
            val habitTypeApp = HabitTypeApp.fromNonNullableHabitType(habit.type)
            tvType.text = root.resources.getString(habitTypeApp.resourceId)
            val habitPriorityApp = HabitPriorityApp.fromHabitPriority(habit.priority)
            tvPriority.text = root.resources.getString(habitPriorityApp.resourceId)
            btnHabitDone.setBackgroundColor(habit.color)
            btnHabitDone.setOnClickListener {
                onButtonHabitDoneClickListener?.invoke(habit)
            }
            val recurrenceNumber = habit.recurrenceNumber
            val recurrenceTimes = root.resources.getQuantityString(
                R.plurals.plurals_times,
                recurrenceNumber,
                recurrenceNumber
            )
            val recurrencePeriod = habit.recurrencePeriod
            val recurrenceDays = root.resources.getQuantityString(
                R.plurals.plurals_days,
                recurrencePeriod,
                recurrencePeriod
            )
            tvRecurrence.text = root.resources.getString(
                R.string.recurrence,
                habit.actualDoneListSize(),
                recurrenceTimes,
                recurrenceDays
            )
            root.setOnClickListener {
                onHabitListClickListener?.invoke(habit)
            }
        }
    }
}