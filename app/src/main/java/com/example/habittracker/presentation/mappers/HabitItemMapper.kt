package com.example.habittracker.presentation.mappers

import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import com.example.habittracker.databinding.FragmentHabitItemBinding
import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.entities.HabitPriority
import com.example.habittracker.domain.entities.HabitType

class HabitItemMapper {

    fun mapViewToHabitItem(binding: FragmentHabitItemBinding): HabitItem {
        with(binding) {
            val name = parseString(tiedName.text)
            val description = parseString(tiedDescription.text)
            val priority = mapSpinnerToHabitPriority(binding.spinnerPriority)
            val type = mapRadioButtonToHabitType(binding.radioGroup)
            val recurrenceNumber = parseNumber(tiedRecurrenceNumber.text)
            val recurrencePeriod = parseNumber(tiedRecurrencePeriod.text)
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

    fun parseString(input: Editable?): String = input?.trim()?.toString() ?: EMPTY_STRING

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