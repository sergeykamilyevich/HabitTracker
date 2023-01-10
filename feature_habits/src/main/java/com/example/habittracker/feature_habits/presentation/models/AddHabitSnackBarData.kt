package com.example.habittracker.feature_habits.presentation.models

import com.example.habittracker.core.domain.models.HabitDone

data class AddHabitSnackBarData(
    val snackbarText: String,
    val habitDone: HabitDone
)
