package com.example.habittracker.presentation.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.HabitListRepositoryImpl
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.usecases.AddHabitItemUseCase
import com.example.habittracker.domain.usecases.EditHabitItemUseCase
import com.example.habittracker.domain.usecases.GetHabitItemUseCase
import com.example.habittracker.presentation.mappers.HabitItemMapper
import kotlinx.coroutines.launch

class HabitItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HabitListRepositoryImpl(application)
    private val addHabitItemUseCase = AddHabitItemUseCase(repository)
    private val editHabitItemUseCase = EditHabitItemUseCase(repository)
    private val getHabitItemUseCase = GetHabitItemUseCase(repository)
    private val mapper = HabitItemMapper()

    private val _habitItem = MutableLiveData<HabitItem>()
    val habitItem: LiveData<HabitItem>
        get() = _habitItem

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputRecurrenceNumber = MutableLiveData<Boolean>()
    val errorInputRecurrenceNumber: LiveData<Boolean>
        get() = _errorInputRecurrenceNumber

    private val _errorInputRecurrencePeriod = MutableLiveData<Boolean>()
    val errorInputRecurrencePeriod: LiveData<Boolean>
        get() = _errorInputRecurrencePeriod

    private val _canCloseScreen = MutableLiveData<Unit>()
    val canCloseScreen: LiveData<Unit>
        get() = _canCloseScreen

    fun addHabitItem(habitItem: HabitItem) {
        val item = HabitItem(
            name = habitItem.name,
            description = habitItem.description,
            priority = habitItem.priority,
            type = habitItem.type,
            color = habitItem.color,
            recurrenceNumber = habitItem.recurrenceNumber,
            recurrencePeriod = habitItem.recurrencePeriod
        )
        viewModelScope.launch {
            addHabitItemUseCase(item)
        }
        closeItemFragment()
    }

    fun editHabitItem(habitItem: HabitItem) {
        _habitItem.value?.let {
            val item = it.copy(
                name = habitItem.name,
                description = habitItem.description,
                priority = habitItem.priority,
                type = habitItem.type,
                color = habitItem.color,
                recurrenceNumber = habitItem.recurrenceNumber,
                recurrencePeriod = habitItem.recurrencePeriod
            )
            viewModelScope.launch {
                editHabitItemUseCase(item)
            }
            closeItemFragment()
        }
    }

    private fun closeItemFragment() {
        _canCloseScreen.value = Unit
    }

    fun getHabitItem(habitItemId: Int) {
        viewModelScope.launch {
            val habitItem = getHabitItemUseCase(habitItemId)
            _habitItem.value = habitItem
        }
    }

    fun validateName(input: CharSequence?) {
        val name = mapper.parseString(input)
        _errorInputName.value = !validateString(name)
    }

    fun validateRecurrenceNumber(input: CharSequence?) {
        val recurrenceNumber = mapper.parseNumber(input)
        _errorInputRecurrenceNumber.value = !validateNumber(recurrenceNumber)
    }

    fun validateRecurrencePeriod(input: CharSequence?) {
        val recurrencePeriod = mapper.parseNumber(input)
        _errorInputRecurrencePeriod.value = !validateNumber(recurrencePeriod)
    }

    private fun validateString(input: String): Boolean = input.isNotBlank()

    private fun validateNumber(input: Int): Boolean = input > 0

}