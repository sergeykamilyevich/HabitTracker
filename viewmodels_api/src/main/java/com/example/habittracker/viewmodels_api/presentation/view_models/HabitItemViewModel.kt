package com.example.habittracker.viewmodels_api.presentation.view_models

import android.text.Editable
import androidx.annotation.ColorInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.core_api.domain.models.Habit

abstract class HabitItemViewModel  : ViewModel() {

    abstract val errorInputName: LiveData<Boolean>

    abstract val errorInputDescription: LiveData<Boolean>

    abstract val errorInputRecurrenceNumber: LiveData<Boolean>

    abstract val errorInputRecurrencePeriod: LiveData<Boolean>

    abstract val currentFragmentHabit: LiveData<Habit>

    abstract val canCloseItemFragment: LiveData<Unit>

    abstract fun currentColor(): Int

    abstract fun saveCurrentColor(@ColorInt color: Int)

    abstract fun validateName(input: Editable?)

    abstract fun validateDescription(input: Editable?)

    abstract fun validateRecurrenceNumber(input: Editable?)

    abstract fun validateRecurrencePeriod(input: Editable?)

    abstract fun chooseScreenMode(habitId: Int)

    abstract fun addHabit(habit: Habit)

    abstract fun upsertHabitItem(habit: Habit)


}