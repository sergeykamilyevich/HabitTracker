package com.example.habittracker.presentation.view_models

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.Time
import com.example.habittracker.domain.models.UpsertException
import com.example.habittracker.domain.usecases.common.SyncUseCase
import com.example.habittracker.domain.usecases.db.DbUseCase
import com.example.habittracker.domain.usecases.network.CloudUseCase
import com.example.habittracker.presentation.mappers.HabitItemMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitItemViewModel @Inject constructor(
    private val syncUseCase: SyncUseCase,
    private val dbUseCase: DbUseCase,
    private val cloudUseCase: CloudUseCase,
    private val mapper: HabitItemMapper,
    private val time: Time
) : ViewModel() {

    private val _habitItem = MutableLiveData<Habit>()
    val habit: LiveData<Habit>
        get() = _habitItem

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

    private val _upsertResult = MutableLiveData<Event<Either<UpsertException, Int>>>()
    val upsertResult: LiveData<Event<Either<UpsertException, Int>>>
        get() = _upsertResult

    fun addHabitItem(habit: Habit) {
        viewModelScope.launch {
            val item = Habit(
                name = habit.name,
                description = habit.description,
                priority = habit.priority,
                type = habit.type,
                color = habit.color,
                recurrenceNumber = habit.recurrenceNumber,
                recurrencePeriod = habit.recurrencePeriod,
                date = time.getCurrentUtcDateInInt()
            )
            Log.d("OkHttp", "addHabitItem item $item")
            upsertHabit(item)
        }
    }

    fun editHabitItem(habit: Habit) {
        _habitItem.value?.let { oldItem ->
            Log.d("OkHttp", "habit $habit")
            viewModelScope.launch {
                val item = oldItem.copy(
                    name = habit.name,
                    description = habit.description,
                    priority = habit.priority,
                    type = habit.type,
                    color = habit.color,
                    recurrenceNumber = habit.recurrenceNumber,
                    recurrencePeriod = habit.recurrencePeriod,
                    date = time.getCurrentUtcDateInInt()
                )
                Log.d("OkHttp", "newItem $item")
                upsertHabit(item)
            }
        }
    }

    private suspend fun upsertHabit(habit: Habit) {
        val resultOfUpserting: Either<UpsertException, Int> =
//            dbUseCase.upsertHabitToDbUseCase(habit)
            syncUseCase.upsertAndPutHabitUseCase(habit)
        when (resultOfUpserting) {
            is Either.Success -> {
                closeItemFragment()
//                val newHabitId = resultOfUpserting.result
//                Log.d("OkHttp", "newHabitId $newHabitId")
//                val apiUid = cloudUseCase.putHabitToCloudUseCase(habit)
//                Log.d("OkHttp", "apiUid $apiUid")
//                apiUid?.let {
//                    val newItemWithNewUid = habit.copy(
//                        id = newHabitId,
//                        apiUid = apiUid
//                    )
//                    Log.d("OkHttp", "newItemWithNewUid $newItemWithNewUid")
//                    dbUseCase.upsertHabitToDbUseCase(newItemWithNewUid)
//                }
            }
            is Either.Failure -> {
                _upsertResult.value = Event(resultOfUpserting)
            }
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