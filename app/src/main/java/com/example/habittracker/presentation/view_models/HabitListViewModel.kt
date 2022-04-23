package com.example.habittracker.presentation.view_models

import android.text.Editable
import android.util.Log
import androidx.lifecycle.*
import com.example.habittracker.di.annotations.MainActivityScope
import com.example.habittracker.domain.models.*
import com.example.habittracker.domain.usecases.common.SyncUseCase
import com.example.habittracker.domain.usecases.db.DbUseCase
import com.example.habittracker.domain.usecases.network.CloudUseCase
import com.example.habittracker.presentation.models.AddHabitDoneResult
import kotlinx.coroutines.launch
import javax.inject.Inject

@MainActivityScope
class HabitListViewModel @Inject constructor(
    private val dbUseCase: DbUseCase,
    private val cloudUseCase: CloudUseCase,
    private val syncUseCase: SyncUseCase
) : ViewModel() {

    private val _habitListFilter = MutableLiveData<HabitListFilter>()
    val habitListFilter: LiveData<HabitListFilter>
        get() = _habitListFilter

    private val _showSnackbarHabitDone = MutableLiveData<Event<AddHabitDoneResult>>()
    val showSnackbarHabitDone: LiveData<Event<AddHabitDoneResult>>
        get() = _showSnackbarHabitDone

    var isHabitDoneButtonsBlocked: Boolean = false
        private set


    private var currentHabitListFilter = HabitListFilter(HabitListOrderBy.NAME_ASC, "")

    lateinit var habitList: LiveData<List<Habit>>

    lateinit var habitListFromApi: List<Habit>

    fun blockHabitDoneButtons() {
        isHabitDoneButtonsBlocked = true
    }

    fun unblockHabitDoneButtons() {
        isHabitDoneButtonsBlocked = false
    }

    fun addHabitItem(habit: Habit) {
        viewModelScope.launch {
            dbUseCase.upsertHabitToDbUseCase(habit)
            cloudUseCase.putHabitToCloudUseCase(habit)
        }
    }

    fun deleteHabitItem(habit: Habit) {
        viewModelScope.launch {
            dbUseCase.deleteHabitFromDbUseCase(habit)
            cloudUseCase.deleteHabitFromCloudUseCase(habit)
        }
    }

    fun deleteAllHabitsFromCloud() {
        viewModelScope.launch {
            cloudUseCase.deleteAllHabitsFromCloudUseCase()
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
            _showSnackbarHabitDone.value = Event(
                AddHabitDoneResult(
                    habit = habitItem,
                    habitDone = newHabitDone
                )
            )
        }
    }

    fun addHabitDoneToCloud(habitDone: HabitDone) {
        viewModelScope.launch {
            cloudUseCase.postHabitDoneToCloudUseCase(habitDone)
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
            habitListFromApi = cloudUseCase.getHabitListFromCloudUseCase() ?: listOf()
            Log.d("OkHttp", "habitListFromApi ${habitListFromApi}")
        }
    }

    fun uploadAllHabitsFromDbToCloud() {
        viewModelScope.launch {
            syncUseCase.syncAllToCloudUseCase()
        }
    }

    fun downloadAllHabitsFromCloudToDb() {
        viewModelScope.launch {
            syncUseCase.syncAllFromCloudUseCase()
        }
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}