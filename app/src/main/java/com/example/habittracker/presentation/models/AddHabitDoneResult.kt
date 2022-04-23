package com.example.habittracker.presentation.models

import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitDone

data class AddHabitDoneResult(
    val habit: Habit,
    val habitDone: HabitDone
)
