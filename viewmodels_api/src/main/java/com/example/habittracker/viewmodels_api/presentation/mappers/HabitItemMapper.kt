package com.example.habittracker.viewmodels_api.presentation.mappers

import android.text.Editable
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.models.HabitPriority
import com.example.habittracker.core_api.domain.models.HabitType
import com.example.habittracker.viewmodels_api.presentation.models.HabitPriorityApp
import com.example.habittracker.viewmodels_api.presentation.models.HabitTypeApp
import com.example.habittracker.viewmodels_api.presentation.models.ViewDataToHabit
import javax.inject.Inject

class HabitItemMapper @Inject constructor() {

    fun mapViewToHabit(viewDataToHabit: ViewDataToHabit): Habit =
        Habit(
            name = parseString(viewDataToHabit.tiedName),
            description = parseString(viewDataToHabit.tiedDescription),
            priority = mapSpinnerToHabitPriority(viewDataToHabit.spinnerPriority),
            type = mapRadioButtonToHabitType(viewDataToHabit.radioGroup),
            color = viewDataToHabit.color,
            recurrenceNumber = parseNumber(viewDataToHabit.tiedRecurrenceNumber),
            recurrencePeriod = parseNumber(viewDataToHabit.tiedRecurrencePeriod)
        )

    private fun mapRadioButtonToHabitType(radioGroup: RadioGroup): HabitType {
        val checkedRadioButtonId = radioGroup.checkedRadioButtonId
        val radioButton = radioGroup.findViewById<RadioButton>(checkedRadioButtonId)
        HabitTypeApp.values().forEach {
            val habitTypeText = radioButton.context.resources.getString(it.resourceId)
            if (habitTypeText == radioButton.text) return it.toNonNullableHabitType()
        }
        throw RuntimeException("Unknown radiobutton selected (id = ${radioButton.id})")
    }

    private fun mapSpinnerToHabitPriority(spinner: Spinner): HabitPriority {
        HabitPriorityApp.values().forEach {
            val habitPriorityText = spinner.context.resources.getString(it.resourceId)
            if (habitPriorityText == spinner.selectedItem) return it.toHabitPriority()
        }
        throw RuntimeException("Unknown spinner selected item: ${spinner.selectedItem}")
    }

    fun parseString(input: Editable?): String =
        input?.trim()?.toString() ?: EMPTY_STRING

    fun parseNumber(input: Editable?): Int {
        return try {
            input?.trim()?.toString()?.toInt() ?: DEFAULT_NUMBER
        } catch (e: Exception) {
            DEFAULT_NUMBER
        }
    }

    fun mapHabitTypeToRadioButton(
        habitType: HabitType,
        rbGoodId: Int,
        rbBadId: Int
    ): Int {
        return when (habitType) {
            HabitType.GOOD -> rbGoodId
            HabitType.BAD -> rbBadId
        }
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val DEFAULT_NUMBER = 0
    }
}