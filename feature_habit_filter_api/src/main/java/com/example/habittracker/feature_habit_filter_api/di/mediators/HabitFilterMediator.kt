package com.example.habittracker.feature_habit_filter_api.di.mediators

import androidx.lifecycle.LiveData
import com.example.habittracker.core_api.domain.models.HabitListFilter

interface HabitFilterMediator {

//    fun filterViewModel(): FilterViewModel

    fun getListFilter(): LiveData<HabitListFilter>
}