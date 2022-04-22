package com.example.habittracker.domain.repositories

import com.example.habittracker.domain.models.*
import kotlinx.coroutines.flow.Flow

interface DbHabitRepository {

    fun getHabitList(
        habitType: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<Habit>>

    suspend fun getUnfilteredList(): List<HabitWithDone>?

    suspend fun getHabitById(habitItemId: Int): Habit

    suspend fun upsertHabit(habit: Habit): Either<UpsertException, Int>

    suspend fun deleteHabit(habit: Habit)

    suspend fun deleteAllHabits()

    suspend fun addHabitDone(habitDone: HabitDone): Int

    suspend fun deleteHabitDone(habitDoneId: Int) //TODO protect against premature uploading habitdones to a cloud
}