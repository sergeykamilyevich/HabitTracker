package com.example.habittracker.domain

import androidx.lifecycle.LiveData
import com.example.habittracker.data.HabitAlreadyExistsException
import com.example.habittracker.domain.entities.HabitDone
import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.entities.HabitListFilter
import com.example.habittracker.domain.entities.HabitType

interface HabitRepository {

    fun getHabitList(
        habitType: HabitType?,
        habitListFilter: HabitListFilter
    ): LiveData<List<HabitItem>>

    suspend fun getHabitById(habitItemId: Int): HabitItem

    suspend fun addHabitItem(habitItem: HabitItem): HabitAlreadyExistsException?

    suspend fun deleteHabitItem(habitItem: HabitItem)

    suspend fun editHabitItem(habitItem: HabitItem): HabitAlreadyExistsException?

    suspend fun addHabitDone(habitDone: HabitDone): Int

    suspend fun deleteHabitDone(habitDoneId: Int)
}