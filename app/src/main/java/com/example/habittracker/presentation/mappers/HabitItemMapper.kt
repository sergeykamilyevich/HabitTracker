package com.example.habittracker.presentation.mappers

import android.graphics.drawable.ColorDrawable
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import com.example.habittracker.databinding.ActivityHabitItemBinding
import com.example.habittracker.domain.HabitItem
import com.example.habittracker.domain.HabitPriority
import com.example.habittracker.domain.HabitType
import java.lang.Exception

class HabitItemMapper {

    fun mapViewToHabitItem(binding: ActivityHabitItemBinding): HabitItem {
        with(binding) {
            val name = parseString(tiedName.text.toString())
            val description = parseString(tiedDescription.text.toString())
            val priority = mapSpinnerToHabitPriority(binding.spinnerPriority)
            val type = mapRadioButtonToHabitType(binding.radioGroup)
            val recurrenceNumber = parseNumber(tiedRecurrenceNumber.text.toString())
            val recurrencePeriod = parseNumber(tiedRecurrencePeriod.text.toString())
            val color = (currentColor.background as ColorDrawable).color
            return HabitItem(
                name,
                description,
                priority,
                type,
                color,
                recurrenceNumber,
                recurrencePeriod
            )
        }
    }

    private fun mapRadioButtonToHabitType(radioGroup: RadioGroup): HabitType {
        val checkedRadioButtonId = radioGroup.checkedRadioButtonId
        val radioButton = radioGroup.findViewById<RadioButton>(checkedRadioButtonId)
        val text = radioButton.text.toString()
        return HabitType.valueOf(text.uppercase())
    }

    private fun mapSpinnerToHabitPriority(spinner: Spinner): HabitPriority {
        return HabitPriority.valueOf(spinner.selectedItem.toString().uppercase())
    }

    fun parseString(input: CharSequence?): String =
        input?.trim()?.toString() ?: EMPTY_STRING

    fun parseNumber(input: CharSequence?): Int {
        return try {
            input?.trim()?.toString()?.toInt() ?: DEFAULT_NUMBER
        } catch (e: Exception) {
            DEFAULT_NUMBER
        }
    }

    fun mapHabitTypeToRadioButton(
        habitType: HabitType,
        binding: ActivityHabitItemBinding
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