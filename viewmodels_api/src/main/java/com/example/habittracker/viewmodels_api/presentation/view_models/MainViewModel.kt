package com.example.habittracker.viewmodels_api.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.models.HabitDone
import com.example.habittracker.core_api.domain.models.HabitType
import com.example.habittracker.viewmodels_api.presentation.models.AddHabitSnackBarData
import com.example.habittracker.viewmodels_api.presentation.tools.Event
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


abstract class MainViewModel : ViewModel() {

    abstract val showSnackbarHabitDone: LiveData<Event<AddHabitSnackBarData>>

    abstract val showResultToast: LiveData<Event<String>>

    abstract val showHabitsAreNotSyncDialogAlert: LiveData<Unit>

    abstract val ioError: LiveData<Event<String>>

    abstract var habitList: LiveData<List<Habit>>

    abstract val cloudError: LiveData<Event<String>>

    abstract fun showErrorToast(error: IoError)

    abstract fun isHabitDoneButtonsBlocked(): Boolean

    abstract fun blockHabitDoneButtons()

    abstract fun unblockHabitDoneButtons()

    abstract fun deleteHabitItem(habit: Habit)

    abstract fun deleteAllHabitsFromCloud()

    abstract fun deleteAllHabitsFromDb()

    abstract fun snackbarText(habit: Habit): String

    abstract fun eventErrorText(error: IoError): Event<String>

    abstract fun addHabitDone(habitDone: HabitDone)

    abstract fun addHabitDoneToCloud(habitDone: HabitDone)

    abstract fun deleteHabitDone(habitDoneId: Int)

    abstract fun getHabitList(habitTypeFilter: HabitType?)

    abstract fun uploadAllHabitsFromDbToCloud()

    abstract fun downloadAllHabitsFromCloudToDb()

    abstract fun snackbarCallback(habitDone: HabitDone): BaseTransientBottomBar.BaseCallback<Snackbar>

    abstract fun compareCloudAndDb()
}