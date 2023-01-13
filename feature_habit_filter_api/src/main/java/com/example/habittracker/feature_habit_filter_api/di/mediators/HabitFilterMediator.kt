package com.example.habittracker.feature_habit_filter_api.di.mediators

import com.example.habittracker.feature_habit_filter_api.presentation.view_models.FilterViewModel

interface HabitFilterMediator {

    fun filterViewModel(): FilterViewModel
}