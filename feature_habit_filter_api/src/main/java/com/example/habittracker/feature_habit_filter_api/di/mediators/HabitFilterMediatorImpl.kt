package com.example.habittracker.feature_habit_filter_api.di.mediators

import androidx.lifecycle.LiveData
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.domain.models.HabitListFilter
import com.example.habittracker.viewmodels.presentation.FilterViewModel
import javax.inject.Inject

@ApplicationScope
class HabitFilterMediatorImpl @Inject constructor(
    private val filterViewModel: FilterViewModel
) : HabitFilterMediator {

//    override fun filterViewModel(): FilterViewModel {
//        return filterViewModel
//    }

    override fun getListFilter(): LiveData<HabitListFilter> {
        return filterViewModel.habitListFilter
    }
}