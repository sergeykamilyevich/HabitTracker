package com.example.habittracker.presentation.recycler

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.databinding.ItemHabitBinding
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.Time
import com.example.habittracker.presentation.models.HabitPriorityApp
import com.example.habittracker.presentation.models.HabitTypeApp


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
            Log.d("99999", "habit $habit")
            Log.d("99999", "habit.actualDoneListSize() ${habit.actualDoneListSize()}")


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