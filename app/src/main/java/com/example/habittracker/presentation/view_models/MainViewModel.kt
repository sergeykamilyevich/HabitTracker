package com.example.habittracker.presentation.view_models

import android.text.Editable
import android.util.Log
import androidx.lifecycle.*
import com.example.habittracker.R
import com.example.habittracker.di.annotations.MainActivityScope
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
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

    private val _showSyncDialogAlert = MutableLiveData<Unit>()
    val showSyncDialogAlert: LiveData<Unit>
        get() = _showSyncDialogAlert

    private val _ioError = MutableLiveData<Event<Either<IoError, Unit>>>()
    val ioError: LiveData<Event<Either<IoError, Unit>>>
        get() = _ioError

    private var isHabitDoneButtonsBlocked: Boolean = false

    private var currentHabitListFilter = HabitListFilter(HabitListOrderBy.NAME_ASC, "")

    lateinit var habitList: LiveData<List<Habit>>

    var errorCloud: LiveData<Either<IoError, Unit>> =
        cloudUseCase.getCloudErrorUseCase.invoke().asLiveData()

    fun isHabitDoneButtonsBlocked() = isHabitDoneButtonsBlocked

    fun setIoError(value: Event<Either<IoError, Unit>>) {
        _ioError.value = value
    }

    fun blockHabitDoneButtons() {
        isHabitDoneButtonsBlocked = true
    }

    fun unblockHabitDoneButtons() {
        isHabitDoneButtonsBlocked = false
    }

    fun deleteHabitItem(habit: Habit) {
        viewModelScope.launch {
            val resultDb = dbUseCase.deleteHabitUseCase.invoke(habit)
            if (resultDb is Failure) _ioError.value = Event(resultDb.error.failure())
            val resultCloud = cloudUseCase.deleteHabitFromCloudUseCase.invoke(habit)
            if (resultCloud is Failure) _ioError.value = Event(resultCloud.error.failure())
        }
    }

    fun deleteAllHabitsFromCloud() {
        viewModelScope.launch {
            val result = cloudUseCase.deleteAllHabitsFromCloudUseCase.invoke()
            if (result is Failure) _ioError.value = Event(result.error.failure())
        }
    }

    fun deleteAllHabitsFromDb() {
        viewModelScope.launch {
            val result = dbUseCase.deleteAllHabitsUseCase.invoke()
            if (result is Failure) _ioError.value = Event(result.error.failure())
        }
    }

    fun addHabitDone(habitDone: HabitDone) {
        viewModelScope.launch {
            val habitDoneIdAdded = dbUseCase.addHabitDoneUseCase.invoke(habitDone)
            when (habitDoneIdAdded) {
                is Success -> {
                    val newHabitDone = habitDone.copy(id = habitDoneIdAdded.result)
                    val habit =
                        dbUseCase.getHabitUseCase.invoke(habitDone.habitId)
                    when (habit) {
                        is Success -> {
                            _showSnackbarHabitDone.value = Event(
                                AddHabitDoneResult(
                                    habit = habit.result,
                                    habitDone = newHabitDone
                                )
                            )
                        }
                        is Failure -> _ioError.value = Event(habit.error.failure())
                    }
                }
                is Failure -> _ioError.value = Event(habitDoneIdAdded.error.failure())
            }
        }
    }

    fun addHabitDoneToCloud(habitDone: HabitDone) {
        viewModelScope.launch {
            val result = cloudUseCase.postHabitDoneToCloudUseCase.invoke(habitDone)
            if (result is Failure) {
                _ioError.value = Event(result.error.failure())
            }
        }
    }

    fun deleteHabitDone(habitDoneId: Int) {
        viewModelScope.launch {
            dbUseCase.deleteHabitDoneUseCase.invoke(habitDoneId)
        }
    }

    fun getHabitList(habitTypeFilter: HabitType?) {
        habitList = Transformations.switchMap(habitListFilter) {
            dbUseCase.getHabitListUseCase.invoke(habitTypeFilter, it).asLiveData()
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

    fun getHabitListFromCloud() {
        viewModelScope.launch {
            val habitList = cloudUseCase.getHabitListFromCloudUseCase.invoke()
            when (habitList) {
                is Success -> Log.d("OkHttp", "habitListFromCloud ${habitList.result}")
                is Failure -> Log.d("OkHttp", "habitListFromCloud error ${habitList.error}")
            }
        }
    }

    fun uploadAllHabitsFromDbToCloud() {
        viewModelScope.launch {
            handleSyncResult(syncUseCase.syncAllToCloudUseCase.invoke())
        }
    }

    fun downloadAllHabitsFromCloudToDb() {
        viewModelScope.launch {
            handleSyncResult(syncUseCase.syncAllFromCloudUseCase.invoke())
        }
    }

    private fun handleSyncResult(result: Either<IoError, Unit>) {
        when (result) {
            is Success -> {
                _showResultToast.value = Event(R.string.sync_is_done.success())
            }
            is Failure -> {
                _showResultToast.value = Event(result.error.failure())
            }
        }
    }

    fun compareCloudAndDb() {
        viewModelScope.launch {
            val result = syncUseCase.areCloudAndDbEqualUseCase.invoke()
            when (result) {
                is Success -> {
                    if (result.result)
                        _showResultToast.value = Event(R.string.cloud_and_db_are_equals.success())
                    else {
                        _showSyncDialogAlert.value = Unit
                    }
                }
                is Failure -> {
                    _showResultToast.value = Event(result.error.failure())
                }
            }
        }
    }


    companion object {
        private const val EMPTY_STRING = ""
    }
}