package com.example.habittracker.domain.repositories

import com.example.habittracker.domain.models.*
import kotlinx.coroutines.flow.Flow

interface DbHabitRepository {

    fun getHabitList(
        habitType: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<HabitItem>>

    suspend fun getHabitById(habitItemId: Int): HabitItem

    suspend fun upsertHabit(habitItem: HabitItem): Either<UpsertException, Int>

    suspend fun deleteHabit(habitItem: HabitItem)

    suspend fun deleteAllHabits()

    suspend fun addHabitDone(habitDone: HabitDone): Int

    suspend fun deleteHabitDone(habitDoneId: Int) //TODO protect against premature uploading habitdones to a cloud
}