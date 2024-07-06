package com.example.habittracker.core_impl.domain.interactor

import com.example.habittracker.core_api.domain.interactor.TimeConvertInteractor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class TimeConvertInteractorImpl @Inject constructor() : TimeConvertInteractor {

    override fun currentUtcDateInSeconds(): Int {
        val currentMoment = Calendar.getInstance()
        val timeInMillis = currentMoment.timeInMillis
        val dateWithoutOffset = timeInMillis / MILLIS_IN_SECOND
        return dateWithoutOffset.toInt()
    }

    override fun utcDateToString(dateInSecond: Int): String {
        val timeInMillis = dateInSecond.toLong() * MILLIS_IN_SECOND
        val locale = Locale.getDefault()
        val sdf = SimpleDateFormat("dd-MM-yyyy", locale)
        return sdf.format(timeInMillis)
    }

    companion object {
        private const val MILLIS_IN_SECOND = 1000
    }
}