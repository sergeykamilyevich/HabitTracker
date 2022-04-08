package com.example.habittracker.domain

import com.example.habittracker.domain.entities.HabitDone
import com.example.habittracker.domain.entities.HabitItem
import com.example.habittracker.domain.entities.HabitListFilter
import com.example.habittracker.domain.entities.HabitType
import kotlinx.coroutines.flow.Flow

interface HabitRepository {

    fun getHabitList(
        habitType: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<HabitItem>>

    suspend fun getHabitById(habitItemId: Int): HabitItem

    suspend fun addHabitItem(habitItem: HabitItem): HabitAlreadyExistsException?

    suspend fun deleteHabitItem(habitItem: HabitItem)

    suspend fun editHabitItem(habitItem: HabitItem): HabitAlreadyExistsException?

    suspend fun addHabitDone(habitDone: HabitDone): Int

    suspend fun deleteHabitDone(habitDoneId: Int)
}