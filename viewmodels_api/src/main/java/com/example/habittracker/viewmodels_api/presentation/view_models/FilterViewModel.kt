package com.example.habittracker.viewmodels_api.presentation.view_models

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.core_api.domain.models.HabitListFilter
import com.example.habittracker.core_api.domain.models.HabitListOrderBy

abstract class FilterViewModel : ViewModel() {

    abstract val habitListFilter: LiveData<HabitListFilter>

    abstract fun updateSearch(input: Editable?)

    abstract fun updateHabitListOrderBy(habitListOrderBy: HabitListOrderBy)
}