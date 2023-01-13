package com.example.habittracker.feature_habit_filter_api.presentation.view_models

import android.text.Editable
import androidx.lifecycle.LiveData
import com.example.habittracker.core_api.domain.models.HabitListFilter
import com.example.habittracker.core_api.domain.models.HabitListOrderBy

interface FilterViewModel {

    val habitListFilter: LiveData<HabitListFilter>

    fun updateSearch(input: Editable?)

    fun updateHabitListOrderBy(habitListOrderBy: HabitListOrderBy)

}