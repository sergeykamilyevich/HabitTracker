package com.example.habittracker.presentation.ui

import com.example.habittracker.domain.models.HabitDone
import com.example.habittracker.presentation.models.AddHabitDoneResult
import com.example.habittracker.presentation.view_models.Event
import com.example.habittracker.presentation.view_models.HabitListViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

class SnackbarCallback constructor(
    private val viewModel: HabitListViewModel,
    private val habitDone: HabitDone
    ): BaseCallback<Snackbar>() {
    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        super.onDismissed(transientBottomBar, event)
        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
            viewModel.addHabitDoneToCloud(habitDone)
        }
        viewModel.unblockHabitDoneButtons()
    }

    override fun onShown(sb: Snackbar?) {
        super.onShown(sb)
        viewModel.blockHabitDoneButtons()
    }
}