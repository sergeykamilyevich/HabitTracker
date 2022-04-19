package com.example.habittracker.presentation.view_models

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.HabitItem
import com.example.habittracker.domain.models.HabitTime
import com.example.habittracker.domain.models.UpsertException
import com.example.habittracker.domain.usecases.db.DbUseCase
import com.example.habittracker.domain.usecases.network.NetworkUseCase
import com.example.habittracker.presentation.mappers.HabitItemMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitItemViewModel @Inject constructor(
    private val dbUseCase: DbUseCase,
    private val networkUseCase: NetworkUseCase,
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

    private val _upsertResult = MutableLiveData<Event<Either<UpsertException, Int>>>()
    val upsertResult: LiveData<Event<Either<UpsertException, Int>>>
        get() = _upsertResult

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
            Log.d("OkHttp", "addHabitItem item $item")
            val resultOfAdding: Either<UpsertException, Int> =
                dbUseCase.upsertHabitToDbUseCase(item)
            if (resultOfAdding is Either.Success) {
                val newHabitId = resultOfAdding.result
            }

            showErrorOrCloseHabitItemScreenSuccessfully(resultOfAdding)
            val apiUid = networkUseCase.putHabitToApiUseCase(item)
            Log.d("OkHttp", "apiUid $apiUid")
            apiUid?.let {
                val newItemWithNewUid = item.copy(
                    apiUid = apiUid
                )
                Log.d("OkHttp", "newItemWithNewUid $newItemWithNewUid")
                dbUseCase.upsertHabitToDbUseCase(newItemWithNewUid)
            }

        }
    }

    fun editHabitItem(habitItem: HabitItem) {
        _habitItem.value?.let {
            Log.d("OkHttp", "habitItem $habitItem")
            viewModelScope.launch {
                val newItem = it.copy(
                    name = habitItem.name,
                    description = habitItem.description,
                    priority = habitItem.priority,
                    type = habitItem.type,
                    color = habitItem.color,
                    recurrenceNumber = habitItem.recurrenceNumber,
                    recurrencePeriod = habitItem.recurrencePeriod,
//                    date = 19102
                )
                Log.d("OkHttp", "newItem $newItem")
                val resultOfEditing = dbUseCase.upsertHabitToDbUseCase(newItem)
                showErrorOrCloseHabitItemScreenSuccessfully(resultOfEditing)
                val apiUid = networkUseCase.putHabitToApiUseCase(newItem)
                Log.d("OkHttp", "apiUid $apiUid")
                apiUid?.let {
                    val newItemWithNewUid = newItem.copy(
                        apiUid = apiUid
                    )
                    Log.d("OkHttp", "newItemWithNewUid $newItemWithNewUid")
                    dbUseCase.upsertHabitToDbUseCase(newItemWithNewUid)
                }
            }
        }
    }

    private fun showErrorOrCloseHabitItemScreenSuccessfully(result: Either<UpsertException, Int>) {
        if (result is Either.Failure) {
            _upsertResult.value = Event(result)
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