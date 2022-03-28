package com.example.habittracker.presentation.view_models

import android.app.Application
import android.text.Editable
import android.util.Log
import androidx.lifecycle.*
import com.example.habittracker.data.HabitListRepositoryImpl
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitListFilter
import com.example.habittracker.domain.HabitListOrderBy
import com.example.habittracker.domain.HabitType
import com.example.habittracker.domain.usecases.*
import kotlinx.coroutines.launch

class HabitListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HabitListRepositoryImpl(application)
    private val getHabitListUseCase = GetHabitListUseCase(repository)
    private val addHabitItemUseCase = AddHabitItemUseCase(repository)
    private val deleteHabitItemUseCase = DeleteHabitItemUseCase(repository)

    private val _habitListFilter = MutableLiveData<HabitListFilter>()
    val habitListFilter: LiveData<HabitListFilter>
        get() = _habitListFilter

    private var currentHabitListFilter = HabitListFilter(HabitListOrderBy.NAME_ASC, "")

    lateinit var habitList : LiveData<List<HabitItem>>

    fun addHabitItem(habitItem: HabitItem) {
        viewModelScope.launch {
            addHabitItemUseCase(habitItem)
        }
    }

    fun deleteHabitItem(habitItem: HabitItem) {
        viewModelScope.launch {
            deleteHabitItemUseCase(habitItem)
        }
    }

    fun getHabitList(habitTypeFilter: HabitType?) {
        habitList = Transformations.switchMap(habitListFilter) {
            getHabitListUseCase(habitTypeFilter, it)
        }
    }

    fun updateHabitListOrderBy(habitListOrderBy: HabitListOrderBy) {
        currentHabitListFilter.orderBy = habitListOrderBy
        _habitListFilter.value = currentHabitListFilter
    }

    fun updateFilter(input: Editable?) {
        currentHabitListFilter.search = input?.toString() ?: EMPTY_STRING
        _habitListFilter.value = currentHabitListFilter
    }

    companion object {
        private const val EMPTY_STRING = "" //TODO double const
    }
}