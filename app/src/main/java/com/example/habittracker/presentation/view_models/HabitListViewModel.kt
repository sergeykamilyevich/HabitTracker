package com.example.habittracker.presentation.view_models

import android.text.Editable
import android.util.Log
import androidx.lifecycle.*
import com.example.habittracker.di.annotations.MainActivityScope
import com.example.habittracker.domain.models.*
import com.example.habittracker.domain.usecases.db.DbUseCase
import com.example.habittracker.domain.usecases.network.NetworkUseCase
import com.example.habittracker.presentation.models.AddHabitDoneResult
import kotlinx.coroutines.launch
import javax.inject.Inject

@MainActivityScope
class HabitListViewModel @Inject constructor(
    private val dbUseCase: DbUseCase,
    private val networkUseCase: NetworkUseCase
) : ViewModel() {

    private val _habitListFilter = MutableLiveData<HabitListFilter>()
    val habitListFilter: LiveData<HabitListFilter>
        get() = _habitListFilter

    private val _showToastHabitDone = MutableLiveData<Event<AddHabitDoneResult>>()
    val showToastHabitDone: LiveData<Event<AddHabitDoneResult>>
        get() = _showToastHabitDone

    var isHabitDoneButtonsBlocked: Boolean = false
        private set


    private var currentHabitListFilter = HabitListFilter(HabitListOrderBy.NAME_ASC, "")

    lateinit var habitList: LiveData<List<HabitItem>>

    lateinit var habitListFromApi: List<HabitItem>

    fun blockHabitDoneButtons() {
        isHabitDoneButtonsBlocked = true
    }

    fun unblockHabitDoneButtons() {
        isHabitDoneButtonsBlocked = false
    }

    fun addHabitItem(habitItem: HabitItem) {
        viewModelScope.launch {
            dbUseCase.upsertHabitToDbUseCase(habitItem)
            networkUseCase.putHabitToApiUseCase(habitItem)
        }
    }

    fun deleteHabitItem(habitItem: HabitItem) {
        viewModelScope.launch {
            dbUseCase.deleteHabitFromDbUseCase(habitItem)
            networkUseCase.deleteHabitFromApiUseCase(habitItem)
        }
    }

    fun deleteAllHabitsFromCloud() {
        viewModelScope.launch {
            networkUseCase.deleteAllHabitsFromApiUseCase()
        }
    }

    fun deleteAllHabitsFromDb() {
        viewModelScope.launch {
            dbUseCase.deleteAllHabitsFromDbUseCase()
        }
    }

    fun addHabitDone(habitDone: HabitDone) {
        viewModelScope.launch {
            val habitDoneIdAdded = dbUseCase.addHabitDoneToDbUseCase(habitDone)
            val newHabitDone = habitDone.copy(id = habitDoneIdAdded)
            val habitItem = dbUseCase.getHabitFromDbUseCase(habitDone.habitId)
            _showToastHabitDone.value = Event(
                AddHabitDoneResult(
                    habitItem = habitItem,
                    habitDone = newHabitDone
                )
            )
        }
    }

    fun addHabitDoneToCloud(habitDone: HabitDone) {
        viewModelScope.launch {
            networkUseCase.postHabitDoneToApiUseCase(habitDone)
        }
    }

    fun deleteHabitDone(habitDoneId: Int) {
        viewModelScope.launch {
            dbUseCase.deleteHabitDoneFromDbUseCase(habitDoneId)
        }
    }

    fun getHabitList(habitTypeFilter: HabitType?) {
        habitList = Transformations.switchMap(habitListFilter) {
            dbUseCase.getHabitListFromDbUseCase(habitTypeFilter, it).asLiveData()
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

    fun fetchHabits() {
        Log.d("OkHttp", "start fetchHabits")
        viewModelScope.launch {
            habitListFromApi = networkUseCase.getHabitListFromApiUseCase() ?: listOf()
            Log.d("OkHttp", "habitListFromApi ${habitListFromApi}")
        }
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}