package com.example.habittracker.core.domain.models

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class Time @Inject constructor() {

    fun currentUtcDateInSeconds(): Int {
        val currentMoment = Calendar.getInstance()
        val timeInMillis = currentMoment.timeInMillis
        val dateWithoutOffset = timeInMillis / MILLIS_IN_SECOND
        return dateWithoutOffset.toInt()
    }

    fun utcDateToString(dateInSecond: Int): String {
        val timeInMillis = dateInSecond.toLong() * MILLIS_IN_SECOND
        val locale = Locale.getDefault()
        val sdf = SimpleDateFormat("dd-MM-yyyy", locale)
        return sdf.format(timeInMillis)
    }

    companion object {
        private const val MILLIS_IN_SECOND = 1000
    }
}