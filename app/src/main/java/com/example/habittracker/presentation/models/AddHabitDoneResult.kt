package com.example.habittracker.presentation.models

import com.example.habittracker.domain.models.HabitDone
import com.example.habittracker.domain.models.Habit

data class AddHabitDoneResult(
    val habit: Habit,
    val habitDone: HabitDone
)
