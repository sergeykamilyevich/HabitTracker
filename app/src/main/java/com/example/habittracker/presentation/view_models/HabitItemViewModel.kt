package com.example.habittracker.presentation.view_models

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.errors.failure
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.Time
import com.example.habittracker.domain.usecases.common.SyncUseCase
import com.example.habittracker.domain.usecases.db.DbUseCase
import com.example.habittracker.presentation.mappers.HabitItemMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitItemViewModel @Inject constructor(
    private val syncUseCase: SyncUseCase,
    private val dbUseCase: DbUseCase,
    private val mapper: HabitItemMapper,
    private val time: Time,
    private val mainViewModel: MainViewModel
) : ViewModel() {

    private val _habit = MutableLiveData<Habit>()
    val habit: LiveData<Habit>
        get() = _habit

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputDescription = MutableLiveData<Boolean>()
    val errorInputDescription: LiveData<Boolean>
        get() = _errorInputDescription

    private val _errorInputRecurrenceNumber = MutableLiveData<Boolean>()
    val errorInputRecurrenceNumber: LiveData<Boolean>
        get() = _errorInputRecurrenceNumber

    private val _errorInputRecurrencePeriod = MutableLiveData<Boolean>()
    val errorInputRecurrencePeriod: LiveData<Boolean>
        get() = _errorInputRecurrencePeriod

    private val _canCloseScreen = MutableLiveData<Unit>()
    val canCloseScreen: LiveData<Unit>
        get() = _canCloseScreen

    fun addHabit(habit: Habit) {
        viewModelScope.launch {
            val item = Habit(
                name = habit.name,
                description = habit.description,
                priority = habit.priority,
                type = habit.type,
                color = habit.color,
                recurrenceNumber = habit.recurrenceNumber,
                recurrencePeriod = habit.recurrencePeriod,
                date = time.currentUtcDateInSeconds()
            )
            upsertHabit(item)
        }
    }

    fun editHabitItem(habit: Habit) {
        _habit.value?.let { oldItem ->
            viewModelScope.launch {
                val item = oldItem.copy(
                    name = habit.name,
                    description = habit.description,
                    priority = habit.priority,
                    type = habit.type,
                    color = habit.color,
                    recurrenceNumber = habit.recurrenceNumber,
                    recurrencePeriod = habit.recurrencePeriod,
                    date = time.currentUtcDateInSeconds()
                )
                upsertHabit(item)
            }
        }
    }

    private suspend fun upsertHabit(habit: Habit) {
        val resultOfUpserting: Either<IoError, Int> =
            dbUseCase.upsertHabitUseCase.invoke(habit)
        when (resultOfUpserting) {
            is Either.Success -> {
                closeItemFragment()
                val newHabitId = resultOfUpserting.result
                val putResult =
                    syncUseCase
                        .putHabitAndSyncWithDbUseCase
                        .invoke(habit = habit, newHabitId = newHabitId)
                if (putResult is Either.Failure) {
                    mainViewModel.setIoError(
                        Event(putResult.error.failure())
                    )
                }
            }
            is Either.Failure -> {
                mainViewModel.setIoError(Event(resultOfUpserting.error.failure()))
            }
        }
    }

    private fun closeItemFragment() {
        _canCloseScreen.value = Unit
    }

    fun getHabit(habitItemId: Int) {
        viewModelScope.launch {
            val habitItem = dbUseCase.getHabitUseCase.invoke(habitItemId)
            when (habitItem) {
                is Either.Success -> {
                    habitItem.result.let {
                        _habit.value = it
                    }
                }
                is Either.Failure -> {
                    mainViewModel.setIoError(Event(habitItem.error.failure()))
                }
            }

        }
    }

    fun validateName(input: Editable?) {
        val name = mapper.parseString(input)
        _errorInputName.value = !validateString(name)
    }

    fun validateDescription(input: Editable?) {
        val name = mapper.parseString(input)
        _errorInputDescription.value = !validateString(name)
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