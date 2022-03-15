package com.example.habittracker.presentation.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.HabitListRepositoryImpl
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.usecases.AddHabitItemUseCase
import com.example.habittracker.domain.usecases.DeleteHabitItemUseCase
import com.example.habittracker.domain.usecases.GetHabitListUseCase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HabitListRepositoryImpl(application)
    private val getHabitListUseCase = GetHabitListUseCase(repository)
    private val addHabitItemUseCase = AddHabitItemUseCase(repository)
    private val deleteHabitItemUseCase = DeleteHabitItemUseCase(repository)
    val habitList = getHabitListUseCase()

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