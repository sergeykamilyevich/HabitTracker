package com.example.habittracker.domain.repositories

import com.example.habittracker.domain.models.*
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