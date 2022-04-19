package com.example.habittracker.presentation.view_models

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.domain.models.HabitItem
import com.example.habittracker.domain.models.HabitTime
import com.example.habittracker.domain.models.UpsertException
import com.example.habittracker.domain.usecases.db.DbUseCase
import com.example.habittracker.domain.usecases.db.GetHabitFromDbUseCase
import com.example.habittracker.domain.usecases.db.UpsertHabitToDbUseCase
import com.example.habittracker.presentation.mappers.HabitItemMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitItemViewModel @Inject constructor(
//    private val upsertHabitToDbUseCase: UpsertHabitToDbUseCase,
//    private val getHabitFromDbUseCase: GetHabitFromDbUseCase,
    private val dbUseCase: DbUseCase,
    private val mapper: HabitItemMapper,
    private val habitTime: HabitTime
) : ViewModel() {

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

    private val _upsertException = MutableLiveData<Event<UpsertException>>()
    val upsertException: LiveData<Event<UpsertException>>
        get() = _upsertException

    fun addHabitItem(habitItem: HabitItem) {
        viewModelScope.launch {
            val item = HabitItem(
                name = habitItem.name,
                description = habitItem.description,
                priority = habitItem.priority,
                type = habitItem.type,
                color = habitItem.color,
                recurrenceNumber = habitItem.recurrenceNumber,
                recurrencePeriod = habitItem.recurrencePeriod,
                date = habitTime.getCurrentUtcDateInInt()
            )
            val isFailureOfAdding = dbUseCase.upsertHabitToDbUseCase(item)
            showErrorOrCloseHabitItemScreenSuccessfully(isFailureOfAdding)
        }
    }

    fun editHabitItem(habitItem: HabitItem) {
        _habitItem.value?.let {
            viewModelScope.launch {
                val item = it.copy(
                    name = habitItem.name,
                    description = habitItem.description,
                    priority = habitItem.priority,
                    type = habitItem.type,
                    color = habitItem.color,
                    recurrenceNumber = habitItem.recurrenceNumber,
                    recurrencePeriod = habitItem.recurrencePeriod
                )
                val isFailureOfEditing = dbUseCase.upsertHabitToDbUseCase(item)
                showErrorOrCloseHabitItemScreenSuccessfully(isFailureOfEditing)
            }
        }
    }

    private fun showErrorOrCloseHabitItemScreenSuccessfully(isFailure: UpsertException?) {
        if (isFailure != null) {
            _upsertException.value = Event(isFailure)
        } else {
            closeItemFragment()
        }
    }

    private fun closeItemFragment() {
        _canCloseScreen.value = Unit
    }

    fun getHabitItem(habitItemId: Int) {
        viewModelScope.launch {
            val habitItem = dbUseCase.getHabitFromDbUseCase(habitItemId)
            _habitItem.value = habitItem
        }
    }

    fun validateName(input: Editable?) {
        val name = mapper.parseString(input)
        _errorInputName.value = !validateString(name)
    }

    fun validateRecurrenceNumber(input: Editable?) {
        val recurrenceNumber = mapper.parseNumber(input)
        _errorInputRecurrenceNumber.value = !validateNumber(recurrenceNumber)
    }

    fun validateRecurrencePeriod(input: Editable?) {
        val recurrencePeriod = mapper.parseNumber(input)
        _errorInputRecurrencePeriod.value = !validateNumber(recurrencePeriod)
    }

    private fun validateString(input: String): Boolean = input.isNotBlank()

    private fun validateNumber(input: Int): Boolean = input > 0
}