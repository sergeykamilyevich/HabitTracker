package com.example.habittracker.presentation.view_models

import android.text.Editable
import android.util.Log
import androidx.lifecycle.*
import com.example.habittracker.R
import com.example.habittracker.di.annotations.MainActivityScope
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.errors.failure
import com.example.habittracker.domain.errors.success
import com.example.habittracker.domain.models.*
import com.example.habittracker.domain.usecases.common.SyncUseCase
import com.example.habittracker.domain.usecases.db.DbUseCase
import com.example.habittracker.domain.usecases.network.CloudUseCase
import com.example.habittracker.presentation.models.AddHabitDoneResult
import kotlinx.coroutines.launch
import javax.inject.Inject

@MainActivityScope
class MainViewModel @Inject constructor(
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

    private val _showResultToast = MutableLiveData<Event<Either<IoError, Int>>>()
    val showResultToast: LiveData<Event<Either<IoError, Int>>>
        get() = _showResultToast

    private var isHabitDoneButtonsBlocked: Boolean = false

    private var currentHabitListFilter = HabitListFilter(HabitListOrderBy.NAME_ASC, "")

    lateinit var habitList: LiveData<List<Habit>>

    var errorCloud: LiveData<Either<IoError, Unit>> =
        cloudUseCase.getCloudErrorUseCase.invoke().asLiveData()

    fun isHabitDoneButtonsBlocked() = isHabitDoneButtonsBlocked

    fun blockHabitDoneButtons() {
        isHabitDoneButtonsBlocked = true
    }

    fun unblockHabitDoneButtons() {
        isHabitDoneButtonsBlocked = false
    }

    fun deleteHabitItem(habit: Habit) {
        viewModelScope.launch {
            dbUseCase.deleteHabitFromDbUseCase(habit)
            cloudUseCase.deleteHabitFromCloudUseCase.invoke(habit)
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
            val habit =
                dbUseCase.getHabitFromDbUseCase.invoke(habitDone.habitId)
            when (habit) {
                is Either.Success -> {
                    _showSnackbarHabitDone.value = Event(
                        AddHabitDoneResult(
                            habit = habit.result,
                            habitDone = newHabitDone
                        )
                    )
                }
                is Either.Failure -> {
                    //TODO
                }
            }
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
            dbUseCase.getHabitListFromDbUseCase.invoke(habitTypeFilter, it).asLiveData()
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
            val habitList = cloudUseCase.getHabitListFromCloudUseCase.invoke()
            when (habitList) {
                is Either.Success -> Log.d("OkHttp", "habitListFromApi ${habitList.result}")
                is Either.Failure -> Log.d("OkHttp", "habitListFromApi error ${habitList.error}")
            }
        }
    }

    fun uploadAllHabitsFromDbToCloud() {
        viewModelScope.launch {
            val result = syncUseCase.syncAllToCloudUseCase.invoke()
            when (result) {
                is Either.Success -> {
                    _showResultToast.value = Event(R.string.sync_is_done.success())
                }
                is Either.Failure -> {
                    _showResultToast.value = Event(result.error.failure())
                }
            }
        }
    }

    fun downloadAllHabitsFromCloudToDb() {
        viewModelScope.launch {
            val result = syncUseCase.syncAllFromCloudUseCase.invoke() //TODO merge with uploadallhabits
            when (result) {
                is Either.Success -> {
                    _showResultToast.value = Event(R.string.sync_is_done.success())
                }
                is Either.Failure -> {
                    _showResultToast.value = Event(result.error.failure())
                }
            }
                    }
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}