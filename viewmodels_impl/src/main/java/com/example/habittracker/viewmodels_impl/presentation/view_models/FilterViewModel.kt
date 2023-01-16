package com.example.habittracker.viewmodels_impl.presentation.view_models

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.domain.models.HabitListFilter
import com.example.habittracker.core_api.domain.models.HabitListOrderBy
import javax.inject.Inject

@ApplicationScope
class FilterViewModel @Inject constructor() : ViewModel()
{

    private var currentHabitListFilter: HabitListFilter = HabitListFilter(HabitListOrderBy.NAME_ASC, "")

    private val _habitListFilter = MutableLiveData<HabitListFilter>()
    val habitListFilter: LiveData<HabitListFilter>
        get() = _habitListFilter

    fun updateSearch(input: Editable?) {
        currentHabitListFilter.search = input?.toString() ?: EMPTY_STRING
        _habitListFilter.value = currentHabitListFilter
    }

    fun updateHabitListOrderBy(habitListOrderBy: HabitListOrderBy) {
        currentHabitListFilter.orderBy = habitListOrderBy
        _habitListFilter.value = currentHabitListFilter
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}