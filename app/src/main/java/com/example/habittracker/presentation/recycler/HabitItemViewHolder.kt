package com.example.habittracker.presentation.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.databinding.ItemHabitBinding
import com.example.habittracker.domain.HabitItem

class HabitItemViewHolder(
    private val binding: ItemHabitBinding,
    private val onHabitListClickListener: ((HabitItem) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(habitItem: HabitItem) {
        with(binding) {
            tvDescription.text = habitItem.description
            tvName.text = habitItem.name
            tvPriority.text = root.resources.getString(habitItem.priority.resourceId)
            tvType.text = root.resources.getString(habitItem.type.resourceId)
            viewColor.setBackgroundColor(habitItem.color)
            val recurrenceNumber = habitItem.recurrenceNumber
            val recurrenceTimes = root.resources.getQuantityString(
                R.plurals.plurals_times,
                recurrenceNumber,
                recurrenceNumber
            )
            val recurrencePeriod = habitItem.recurrencePeriod
            val recurrenceDays = root.resources.getQuantityString(
                R.plurals.plurals_days,
                recurrencePeriod,
                recurrencePeriod
            )
            tvRecurrence.text = root.resources.getString(
                R.string.recurrence,
                recurrenceTimes,
                recurrenceDays
            )
            root.setOnClickListener {
                onHabitListClickListener?.invoke(habitItem)
            }
        }
    }
}