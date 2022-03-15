package com.example.habittracker.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.habittracker.R
import com.example.habittracker.domain.HabitItem

class HabitListAdapter : ListAdapter<HabitItem, HabitItemViewHolder>(HabitItemDiffCallback()) {

    var onHabitListClickListener: ((HabitItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_habit,
            parent,
            false
        )
        return HabitItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: HabitItemViewHolder, position: Int) {
        val habitItem = getItem(position)

        with(viewHolder.binding) {
            tvDescription.text = habitItem.description
            tvName.text = habitItem.name
            tvPriority.text = habitItem.priority.toString()
            tvType.text = habitItem.type.toString()
            viewColor.setBackgroundColor(habitItem.color)
            val recurrenceNumber = habitItem.recurrenceNumber
            val pluralWordEndingNumber = pluralWordEnding(recurrenceNumber)
            val recurrencePeriod = habitItem.recurrencePeriod
            val pluralWordEndingPeriod = pluralWordEnding(recurrencePeriod)
            tvRecurrence.text = root.resources.getString(
                R.string.recurrence_time,
                recurrenceNumber,
                pluralWordEndingNumber,
                recurrencePeriod,
                pluralWordEndingPeriod
            )
            root.setOnClickListener {
                onHabitListClickListener?.invoke(habitItem)
            }
        }
    }

    private fun pluralWordEnding(int: Int): String = if (int > 1) "s" else ""

}