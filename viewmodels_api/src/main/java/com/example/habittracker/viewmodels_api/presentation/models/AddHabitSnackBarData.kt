package com.example.habittracker.viewmodels_api.presentation.models

import com.example.habittracker.core_api.domain.models.HabitDone

data class AddHabitSnackBarData(
    val snackbarText: String,
    val habitDone: HabitDone
)
