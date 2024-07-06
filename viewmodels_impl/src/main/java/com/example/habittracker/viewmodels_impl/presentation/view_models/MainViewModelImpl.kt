package com.example.habittracker.viewmodels_impl.presentation.view_models

import android.util.Log
import androidx.lifecycle.*
import com.example.habittracker.cloud_sync.domain.usecases.interfaces.SyncUseCase
import com.example.habittracker.core_api.di.annotations.ApplicationScope
import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.Either.Failure
import com.example.habittracker.core_api.domain.errors.Either.Success
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.errors.IoError.*
import com.example.habittracker.core_api.domain.interactor.TimeConvertInteractor
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.models.HabitDone
import com.example.habittracker.core_api.domain.models.HabitType
import com.example.habittracker.db_api.domain.usecases.DbUseCase
import com.example.habittracker.network_api.domain.usecases.CloudUseCase
import com.example.habittracker.ui_kit.R
import com.example.habittracker.viewmodels_api.presentation.models.AddHabitSnackBarData
import com.example.habittracker.viewmodels_api.presentation.tools.Event
import com.example.habittracker.viewmodels_api.presentation.tools.Resources
import com.example.habittracker.viewmodels_api.presentation.view_models.MainViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@ApplicationScope
class MainViewModelImpl @Inject constructor(
    private val dbUseCase: DbUseCase,
    private val cloudUseCase: CloudUseCase,
    private val syncUseCase: SyncUseCase,
    private val resources: Resources,
    private val filterViewModel: FilterViewModelImpl,
    private val timeConvertInteractor: TimeConvertInteractor,
) : MainViewModel() {

    private val _showSnackbarHabitDone = MutableLiveData<Event<AddHabitSnackBarData>>()
    override val showSnackbarHabitDone: LiveData<Event<AddHabitSnackBarData>>
        get() = _showSnackbarHabitDone

    private val _showResultToast = MutableLiveData<Event<String>>()
    override val showResultToast: LiveData<Event<String>>
        get() = _showResultToast

    private val _showHabitsAreNotSyncDialogAlert = MutableLiveData<Unit>()
    override val showHabitsAreNotSyncDialogAlert: LiveData<Unit>
        get() = _showHabitsAreNotSyncDialogAlert

    private val _ioError = MutableLiveData<Event<String>>()
    override val ioError: LiveData<Event<String>>
        get() = _ioError

    private var isHabitDoneButtonsBlocked: Boolean = false

    override lateinit var habitList: LiveData<List<Habit>>

    private val errorFlow by lazy {
        cloudUseCase.getCloudErrorUseCase.invoke()
    }
    override val cloudError: LiveData<Event<String>> by lazy {
        MediatorLiveData<Event<String>>().apply {
            addSource(errorFlow.asLiveData()) {
                Log.e("ErrorApp", "Flow $it")
                if (it is Failure) value = eventErrorText(it.error)
            }
        }
    }

    override fun showErrorToast(error: IoError) {
        _ioError.value = eventErrorText(error)
    }

    override fun isHabitDoneButtonsBlocked() = isHabitDoneButtonsBlocked

    override fun blockHabitDoneButtons() {
        isHabitDoneButtonsBlocked = true
    }

    override fun unblockHabitDoneButtons() {
        isHabitDoneButtonsBlocked = false
    }

    override fun deleteHabitItem(habit: Habit) {
        viewModelScope.launch {
            val resultDb = dbUseCase.deleteHabitUseCase.invoke(habit)
            if (resultDb is Failure) _ioError.value = eventErrorText(resultDb.error)
            val resultCloud = cloudUseCase.deleteHabitFromCloudUseCase.invoke(habit)
            if (resultCloud is Failure) _ioError.value = eventErrorText(resultCloud.error)
        }
    }

    override fun deleteAllHabitsFromCloud() {
        viewModelScope.launch {
            val result = cloudUseCase.deleteAllHabitsFromCloudUseCase.invoke()
            if (result is Failure) {
                _ioError.value = eventErrorText(result.error)
                Log.e("ErrorApp", result.toString())
            }
        }
    }

    override fun deleteAllHabitsFromDb() {
        viewModelScope.launch {
            val result = dbUseCase.deleteAllHabitsUseCase.invoke()
            if (result is Failure) {
                _ioError.value = eventErrorText(result.error)
                Log.e("ErrorApp", result.toString())
            }
        }
    }

    override fun snackbarText(habit: Habit): String {
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

    override fun eventErrorText(error: IoError): Event<String> {
        Log.e("ErrorApp eventErrorText", error.toString())
        val errorTextFromRes = when (error) {
            is HabitAlreadyExistsError -> R.string.habit_already_exists
            is SqlError -> R.string.sql_error
            is CloudError -> R.string.cloud_error
            is DeletingAllHabitsError -> R.string.deleting_habits_error
            is DeletingHabitError -> R.string.deleting_habits_error
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


    override fun addHabitDone(habitDone: HabitDone) {
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

    override fun addHabitDoneToCloud(habitDone: HabitDone) {
        viewModelScope.launch {
            val result = cloudUseCase.postHabitDoneToCloudUseCase.invoke(habitDone)
            if (result is Failure) {
                _ioError.value = eventErrorText(result.error)
            }
        }
    }

    override fun deleteHabitDone(habitDoneId: Int) {
        viewModelScope.launch {
            dbUseCase.deleteHabitDoneUseCase.invoke(habitDoneId)
        }
    }

    override fun getHabitList(habitTypeFilter: HabitType?) {
        habitList = Transformations.switchMap(filterViewModel.habitListFilter) {
            dbUseCase.getHabitListUseCase.invoke(habitTypeFilter, it).asLiveData()
        }
    }

    override fun uploadAllHabitsFromDbToCloud() {
        viewModelScope.launch {
            handleSyncResult(syncUseCase.syncAllToCloudUseCase.invoke())
        }
    }

    override fun downloadAllHabitsFromCloudToDb() {
        viewModelScope.launch {
            handleSyncResult(syncUseCase.syncAllFromCloudUseCase.invoke())
        }
    }

    override fun snackbarCallback(habitDone: HabitDone) =
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

    override fun compareCloudAndDb() {
        viewModelScope.launch {
            val result = syncUseCase.areCloudAndDbEqualUseCase.invoke()
            when (result) {
                is Success -> {
                    if (result.result)
                        _showResultToast.value =
                            Event(resources.getString(R.string.cloud_and_db_are_equals))
                    else {
                        _showHabitsAreNotSyncDialogAlert.value = Unit
                    }
                }
                is Failure -> {
                    _showResultToast.value = eventErrorText(result.error)
                }
            }
        }
    }

    override fun currentUtcDateInSeconds(): Int = timeConvertInteractor.currentUtcDateInSeconds()

    companion object {
        private const val EMPTY_CODE_STRING = ""
        private const val UNKNOWN_CODE = 0
    }
}

