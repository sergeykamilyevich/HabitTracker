package com.example.habittracker.presentation.models

import com.example.habittracker.domain.models.HabitDone

data class AddHabitSnackBarData(
    val snackbarText: String,
    val habitDone: HabitDone
)
