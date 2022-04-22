package com.example.habittracker.presentation.mappers

import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import com.example.habittracker.databinding.FragmentHabitItemBinding
import com.example.habittracker.di.annotations.MainActivityScope
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitPriority
import com.example.habittracker.domain.models.HabitType
import com.example.habittracker.presentation.models.HabitPriorityApp
import com.example.habittracker.presentation.models.HabitTypeApp
import javax.inject.Inject

@MainActivityScope
class HabitItemMapper @Inject constructor() {

    fun mapViewToHabitItem(binding: FragmentHabitItemBinding): Habit = with(binding) {
        Habit(
            name = parseString(tiedName.text),
            description = parseString(tiedDescription.text),
            priority = mapSpinnerToHabitPriority(binding.spinnerPriority),
            type = mapRadioButtonToHabitType(binding.radioGroup),
            color = (currentColor.background as ColorDrawable).color,
            recurrenceNumber = parseNumber(tiedRecurrenceNumber.text),
            recurrencePeriod = parseNumber(tiedRecurrencePeriod.text)
        )
    }

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
        input?.trim()?.toString() ?: EMPTY_STRING //TODO replace in other class

    fun parseNumber(input: Editable?): Int {
        return try {
            input?.trim()?.toString()?.toInt() ?: DEFAULT_NUMBER
        } catch (e: Exception) {
            DEFAULT_NUMBER
        }
    }

    fun mapHabitTypeToRadioButton(
        habitType: HabitType,
        binding: FragmentHabitItemBinding
    ): Int {
        return when (habitType) {
            HabitType.GOOD -> binding.rbGood.id
            HabitType.BAD -> binding.rbBad.id
        }
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val DEFAULT_NUMBER = 0
    }
}