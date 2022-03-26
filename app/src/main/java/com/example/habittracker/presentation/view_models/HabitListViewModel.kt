package com.example.habittracker.presentation.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.HabitListRepositoryImpl
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitType
import com.example.habittracker.domain.usecases.*
import kotlinx.coroutines.launch

class HabitListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HabitListRepositoryImpl(application)
    private val getHabitListUseCase = GetHabitListUseCase(repository)
    private val getHabitFilteredListUseCase = GetHabitFilteredListUseCase(repository)
    private val addHabitItemUseCase = AddHabitItemUseCase(repository)
    private val deleteHabitItemUseCase = DeleteHabitItemUseCase(repository)
    var habitList = getHabitFilteredListUseCase(null)
    val habitGoodList = getHabitFilteredListUseCase(HabitType.GOOD)
    val habitBadList = getHabitFilteredListUseCase(HabitType.BAD)

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

    fun getFilteredHabitList(habitType: HabitType) {
        habitList = getHabitFilteredListUseCase(habitType)
    }

}