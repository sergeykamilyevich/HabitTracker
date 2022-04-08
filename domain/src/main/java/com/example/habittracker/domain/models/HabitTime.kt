package com.example.habittracker.domain.models

import java.text.SimpleDateFormat
import java.util.*

class HabitTime {

    fun getCurrentUtcDateInInt(): Int {
        val currentMoment = Calendar.getInstance()
        val timeInMillis = currentMoment.timeInMillis
        val dateWithoutOffset = timeInMillis / MILLIS_IN_DAY
        return dateWithoutOffset.toInt()
    }

    fun mapUtcDateInIntToString(date: Int): String {
        val currentMoment = Calendar.getInstance()
        val offsetInMillis = currentMoment.timeZone.rawOffset
        val timeInMillis = date.toLong() * MILLIS_IN_DAY + offsetInMillis
        val locale = Locale.getDefault()
        val sdf = SimpleDateFormat("dd-MM-yyyy", locale)
        return sdf.format(timeInMillis)
    }

    companion object {
        const val MILLIS_IN_DAY = 24 * 60 * 60 * 1000

    }
}