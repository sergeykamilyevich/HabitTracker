package com.example.habittracker.presentation.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.databinding.ItemHabitBinding

class HabitItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemHabitBinding.bind(view)
}