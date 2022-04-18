package com.example.habittracker.presentation.models

import com.example.habittracker.domain.models.HabitItem

data class AddHabitDoneResult(
    val habitItem: HabitItem,
    val habitDoneId: Int
)
