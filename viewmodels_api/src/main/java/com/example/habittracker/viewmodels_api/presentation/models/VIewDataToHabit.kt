package com.example.habittracker.viewmodels_api.presentation.models

import android.text.Editable
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.annotation.ColorInt

data class ViewDataToHabit(
    val tiedName: Editable?,
    val tiedDescription: Editable?,
    val spinnerPriority: Spinner,
    val radioGroup: RadioGroup,
    @ColorInt val color: Int,
    val tiedRecurrenceNumber: Editable?,
    val tiedRecurrencePeriod: Editable?
)
