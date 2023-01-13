package com.example.habittracker.feature_habit_filter_impl.presentation.view_models

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.domain.models.HabitListFilter
import com.example.habittracker.core_api.domain.models.HabitListOrderBy
import com.example.habittracker.feature_habit_filter_api.presentation.view_models.BottomSheetViewModel
import javax.inject.Inject

@ApplicationScope
class BottomSheetViewModelImpl @Inject constructor(

) : ViewModel(), BottomSheetViewModel {

    private var currentHabitListFilter: HabitListFilter = HabitListFilter(HabitListOrderBy.NAME_ASC, "")

    private val _habitListFilter = MutableLiveData<HabitListFilter>()
    override val habitListFilter: LiveData<HabitListFilter>
        get() = _habitListFilter

    override fun updateSearch(input: Editable?) {
        currentHabitListFilter.search = input?.toString() ?: EMPTY_STRING
        _habitListFilter.value = currentHabitListFilter
    }

    override fun updateHabitListOrderBy(habitListOrderBy: HabitListOrderBy) {
        currentHabitListFilter.orderBy = habitListOrderBy
        _habitListFilter.value = currentHabitListFilter
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}