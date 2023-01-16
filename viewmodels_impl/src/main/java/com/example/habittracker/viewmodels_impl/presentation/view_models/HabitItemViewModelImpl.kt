package com.example.habittracker.viewmodels_impl.presentation.view_models

import android.text.Editable
import android.util.Log
import androidx.annotation.ColorInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.habittracker.cloud_sync.domain.usecases.interfaces.SyncUseCase
import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.Either.Failure
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.models.HabitPriority
import com.example.habittracker.core_api.domain.models.HabitType
import com.example.habittracker.core_api.domain.models.Time
import com.example.habittracker.db_api.domain.usecases.DbUseCase
import com.example.habittracker.viewmodels_api.presentation.color.ColorPicker
import com.example.habittracker.viewmodels_api.presentation.mappers.HabitItemMapper
import com.example.habittracker.viewmodels_api.presentation.view_models.HabitItemViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitItemViewModelImpl @Inject constructor(
    private val mapper: HabitItemMapper,
    private val mainViewModel: MainViewModelImpl,
    private val time: Time,
    private val colorPicker: ColorPicker,
    private val dbUseCase: DbUseCase,
    private val syncUseCase: SyncUseCase
) : HabitItemViewModel() {

    private val _errorInputName = MutableLiveData<Boolean>()
    override val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputDescription = MutableLiveData<Boolean>()
    override val errorInputDescription: LiveData<Boolean>
        get() = _errorInputDescription

    private val _errorInputRecurrenceNumber = MutableLiveData<Boolean>()
    override val errorInputRecurrenceNumber: LiveData<Boolean>
        get() = _errorInputRecurrenceNumber

    private val _errorInputRecurrencePeriod = MutableLiveData<Boolean>()
    override val errorInputRecurrencePeriod: LiveData<Boolean>
        get() = _errorInputRecurrencePeriod

    private val _currentFragmentHabit = MutableLiveData<Habit>()
    override val currentFragmentHabit: LiveData<Habit>
        get() = _currentFragmentHabit


    private val _canCloseItemFragment = MutableLiveData<Unit>()
    override val canCloseItemFragment: LiveData<Unit>
        get() = _canCloseItemFragment

    private var currentColor = colorPicker.colors()[0]
    override fun currentColor(): Int = currentColor

    override fun saveCurrentColor(@ColorInt color: Int) {
        currentColor = color
    }

    override fun validateName(input: Editable?) {
        val name = mapper.parseString(input)
        _errorInputName.value = !validateString(name)
    }

    override fun validateDescription(input: Editable?) {
        val name = mapper.parseString(input)
        _errorInputDescription.value = !validateString(name)
    }

    override fun validateRecurrenceNumber(input: Editable?) {
        val recurrenceNumber = mapper.parseNumber(input)
        _errorInputRecurrenceNumber.value = !validateNumber(recurrenceNumber)
    }

    override fun validateRecurrencePeriod(input: Editable?) {
        val recurrencePeriod = mapper.parseNumber(input)
        _errorInputRecurrencePeriod.value = !validateNumber(recurrencePeriod)
    }

    private fun validateString(input: String): Boolean = input.isNotBlank()

    private fun validateNumber(input: Int): Boolean = input > 0

    override fun chooseScreenMode(habitId: Int) {
        if (habitId == Habit.UNDEFINED_ID) setEmptyCurrentHabit()
        else setCurrentHabitFromDb(habitId)
    }

    private fun setEmptyCurrentHabit() {
        _currentFragmentHabit.value = Habit(
            name = EMPTY_STRING,
            description = EMPTY_STRING,
            priority = HabitPriority.NORMAL,
            type = HabitType.GOOD,
            color = colorPicker.colors()[0],
            recurrenceNumber = 0,
            recurrencePeriod = 0
        )
    }

    private fun setCurrentHabitFromDb(habitId: Int) {
        viewModelScope.launch {
            val habit = dbUseCase.getHabitUseCase.invoke(habitId)
            when (habit) {
                is Success -> {
                    habit.result.let {
                        _currentFragmentHabit.value = it
                    }
                }
                is Failure -> {
                    mainViewModel.showErrorToast(habit.error)
                }
            }
        }
    }

    override fun addHabit(habit: Habit) {
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

    override fun upsertHabitItem(habit: Habit) {
        _currentFragmentHabit.value?.let { oldItem ->
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
        val resultOfUpserting: Either<IoError, Int> = dbUseCase.upsertHabitUseCase.invoke(habit)
        when (resultOfUpserting) {
            is Success -> {
                val habitToPut = habit.copy(id = resultOfUpserting.result)
                val putResult =
                    syncUseCase.putHabitAndSyncWithDbUseCase.invoke(habitToPut)
                Log.d("ErrorApp", "putResult $putResult")
                if (putResult is Failure) {
                    mainViewModel.showErrorToast(putResult.error)
                }
                closeItemFragment()
            }
            is Failure -> mainViewModel.showErrorToast(resultOfUpserting.error)
        }
    }

    private fun closeItemFragment() {
        _canCloseItemFragment.value = Unit
    }

    companion object {
        private const val EMPTY_STRING = ""
    }

}