package com.example.habittracker.core_api.domain.interactor

interface TimeConvertInteractor {

    fun currentUtcDateInSeconds(): Int

    fun utcDateToString(dateInSecond: Int): String
}