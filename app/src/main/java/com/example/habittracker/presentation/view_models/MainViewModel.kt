package com.example.habittracker.presentation.view_models

import android.text.Editable
import android.util.Log
import androidx.lifecycle.*
import com.example.habittracker.R
import com.example.habittracker.di.annotations.MainViewModelScope
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.Either.Failure
import com.example.habittracker.domain.errors.Either.Success
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.models.*
import com.example.habittracker.domain.usecases.common.SyncUseCase
import com.example.habittracker.domain.usecases.db.DbUseCase
import com.example.habittracker.domain.usecases.network.CloudUseCase
import com.example.habittracker.presentation.models.AddHabitSnackBarData
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@MainViewModelScope
class MainViewModel @Inject constructor(
    private val dbUseCase: DbUseCase,
    private val cloudUseCase: CloudUseCase,
    private val syncUseCase: SyncUseCase,
    private val resources: Resources,
) : ViewModel() {

    init {
//        compareCloudAndDb() //TODO where should it be?
    }

    private val _habitListFilter = MutableLiveData<HabitListFilter>()
    val habitListFilter: LiveData<HabitListFilter>
        get() = _habitListFilter

    private val _showSnackbarHabitDone = MutableLiveData<Event<AddHabitSnackBarData>>()
    val showSnackbarHabitDone: LiveData<Event<AddHabitSnackBarData>>
        get() = _showSnackbarHabitDone

    private val _showResultToast = MutableLiveData<Event<String>>()
    val showResultToast: LiveData<Event<String>>
        get() = _showResultToast

    private val _showSyncDialogAlert = MutableLiveData<Unit>()
    val showSyncDialogAlert: LiveData<Unit>
        get() = _showSyncDialogAlert

    private val _ioError = MutableLiveData<Event<String>>()
    val ioError: LiveData<Event<String>>
        get() = _ioError

    private var isHabitDoneButtonsBlocked: Boolean = false

    private var currentHabitListFilter = HabitListFilter(HabitListOrderBy.NAME_ASC, "")

    lateinit var habitList: LiveData<List<Habit>>

    private val errorFlow = cloudUseCase.getCloudErrorUseCase.invoke()
    val cloudError: LiveData<Event<String>> = MediatorLiveData<Event<String>>().apply {
        addSource(errorFlow.asLiveData()) {
            Log.d("ErrorApp", "Flow $it")
            if (it is Failure) value = eventErrorText(it.error)
        }
    }

    fun showErrorToast(error: IoError) {
        _ioError.value = eventErrorText(error)
    }

    fun isHabitDoneButtonsBlocked() = isHabitDoneButtonsBlocked

    fun blockHabitDoneButtons() {
        isHabitDoneButtonsBlocked = true
    }

    fun unblockHabitDoneButtons() {
        isHabitDoneButtonsBlocked = false
    }

    fun deleteHabitItem(habit: Habit) {
        viewModelScope.launch {
            val resultDb = dbUseCase.deleteHabitUseCase.invoke(habit)
            if (resultDb is Failure) _ioError.value = eventErrorText(resultDb.error)
            val resultCloud = cloudUseCase.deleteHabitFromCloudUseCase.invoke(habit)
            if (resultCloud is Failure) _ioError.value = eventErrorText(resultCloud.error)
        }
    }

    fun deleteAllHabitsFromCloud() {
        viewModelScope.launch {
            val result = cloudUseCase.deleteAllHabitsFromCloudUseCase.invoke()
            if (result is Failure) {
                _ioError.value = eventErrorText(result.error)
                Log.d("ErrorApp", result.toString())
            }
        }
    }

    fun deleteAllHabitsFromDb() {
        viewModelScope.launch {
            val result = dbUseCase.deleteAllHabitsUseCase.invoke()
            if (result is Failure) _ioError.value = eventErrorText(result.error)
            Log.d("ErrorApp", result.toString())
        }
    }

    private fun snackbarText(habit: Habit): String {
        val habitType = habit.type
        val habitRecurrenceNumber = habit.recurrenceNumber
        val actualDoneListSize = habit.actualDoneListSize()
        val differenceDone = abs(actualDoneListSize - habitRecurrenceNumber)
        val differenceDoneTimes = resources.getQuantityString(
            R.plurals.plurals_more_times,
            differenceDone,
            differenceDone
        )
        return when (habitType) {
            HabitType.GOOD -> {
                if (actualDoneListSize < habitRecurrenceNumber)
                    resources.getString(R.string.worth_doing_more_times, differenceDoneTimes)
                else resources.getString(R.string.you_are_breathtaking)
            }
            HabitType.BAD -> {
                if (actualDoneListSize < habitRecurrenceNumber)
                    resources.getString(R.string.you_are_allowed_more_times, differenceDoneTimes)
                else resources.getString(R.string.stop_doing_it)
            }
        }
    }

    private fun eventErrorText(error: IoError): Event<String> {
        Log.e("ErrorApp eventErrorText", error.toString())
        val errorTextFromRes = when (error) {
            is IoError.HabitAlreadyExistsError -> R.string.habit_already_exists
            is IoError.SqlError -> R.string.sql_error
            is IoError.CloudError -> R.string.cloud_error
            is IoError.DeletingAllHabitsError -> R.string.deleting_habits_error
            is IoError.DeletingHabitError -> R.string.deleting_habits_error
        }
        val errorText = resources.getString(
            errorTextFromRes,
            error.message,
            errorCodeText(error.code)
        )
        Log.e("ErrorApp", errorText)
        return Event(errorText)
    }

    private fun errorCodeText(code: Int): String =
        if (code != UNKNOWN_CODE) ", code: $code"
        else EMPTY_CODE_STRING


    fun addHabitDone(habitDone: HabitDone) {
        viewModelScope.launch {
            val habitDoneIdAdded = dbUseCase.addHabitDoneUseCase.invoke(habitDone)
            when (habitDoneIdAdded) {
                is Success -> {
                    val newHabitDone = habitDone.copy(id = habitDoneIdAdded.result)
                    val habit =
                        dbUseCase.getHabitUseCase.invoke(habitDone.habitId)
                    when (habit) {
                        is Success -> _showSnackbarHabitDone.value = Event(
                            AddHabitSnackBarData(snackbarText(habit.result), newHabitDone)
                        )
                        is Failure -> _ioError.value = eventErrorText(habit.error)
                    }
                }
                is Failure -> _ioError.value = eventErrorText(habitDoneIdAdded.error)
            }
        }
    }

    fun addHabitDoneToCloud(habitDone: HabitDone) {
        viewModelScope.launch {
            val result = cloudUseCase.postHabitDoneToCloudUseCase.invoke(habitDone)
            if (result is Failure) {
                _ioError.value = eventErrorText(result.error)
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

    fun snackbarCallback(habitDone: HabitDone) =
        object : BaseTransientBottomBar.BaseCallback<Snackbar>() {

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                    addHabitDoneToCloud(habitDone)
                }
                unblockHabitDoneButtons()
            }

            override fun onShown(sb: Snackbar?) {
                super.onShown(sb)
                blockHabitDoneButtons()
            }
        }


    private fun handleSyncResult(result: Either<IoError, Unit>) {
        _showResultToast.value = when (result) {
            is Success -> {
                Event(resources.getString(R.string.sync_is_done))
            }
            is Failure -> {
                eventErrorText(result.error)
            }
        }
    }

    fun compareCloudAndDb() {
        viewModelScope.launch {
            val result = syncUseCase.areCloudAndDbEqualUseCase.invoke()
            when (result) {
                is Success -> {
                    if (result.result)
                        _showResultToast.value =
                            Event(resources.getString(R.string.cloud_and_db_are_equals))
                    else {
                        _showSyncDialogAlert.value = Unit
                    }
                }
                is Failure -> {
                    _showResultToast.value = eventErrorText(result.error)
                }
            }
        }
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val EMPTY_CODE_STRING = ""
        private const val UNKNOWN_CODE = 0
    }
}