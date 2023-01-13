package com.example.habittracker.feature_habit_filter_impl.di.mediators

import com.example.habittracker.feature_habit_filter_api.di.mediators.HabitFilterMediator
import com.example.habittracker.feature_habit_filter_api.presentation.view_models.BottomSheetViewModel
import com.example.habittracker.feature_habit_filter_impl.presentation.view_models.BottomSheetViewModelImpl
import javax.inject.Inject

class HabitFilterMediatorImpl @Inject constructor(
    private val bottomSheetViewModel: BottomSheetViewModelImpl
) : HabitFilterMediator {

    override fun getBottomSheetViewModel(): BottomSheetViewModel = bottomSheetViewModel
}