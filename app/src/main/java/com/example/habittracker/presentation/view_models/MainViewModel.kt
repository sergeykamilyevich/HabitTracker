package com.example.habittracker.presentation.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.HabitListRepositoryImpl
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.usecases.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HabitListRepositoryImpl(application)
    private val getHabitListUseCase = GetHabitListUseCase(repository)
    private val getHabitGoodListUseCase = GetHabitGoodListUseCase(repository)
    private val getHabitBadListUseCase = GetHabitBadListUseCase(repository)
    private val addHabitItemUseCase = AddHabitItemUseCase(repository)
    private val deleteHabitItemUseCase = DeleteHabitItemUseCase(repository)
    val habitList = getHabitListUseCase()
    val habitGoodList = getHabitGoodListUseCase()
    val habitBadList = getHabitBadListUseCase()

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

}