package com.example.habittracker.feature_habit_filter_impl.di.mediators

import com.example.habittracker.feature_habit_filter_api.di.mediators.HabitFilterMediator
import com.example.habittracker.feature_habit_filter_api.presentation.view_models.FilterViewModel
import com.example.habittracker.feature_habit_filter_impl.presentation.view_models.FilterViewModelImpl
import javax.inject.Inject

class HabitFilterMediatorImpl @Inject constructor(
    private val filterViewModel: FilterViewModelImpl
) : HabitFilterMediator {

    override fun filterViewModel(): FilterViewModel = filterViewModel
}