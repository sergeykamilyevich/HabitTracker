package com.example.habittracker.domain.repositories

import com.example.habittracker.domain.models.*
import kotlinx.coroutines.flow.Flow

interface DbHabitRepository {

    fun getHabitList(
        habitType: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<Habit>>

    suspend fun getUnfilteredList(): List<Habit>? //TODO refactor to non-null

    suspend fun getHabitById(habitItemId: Int): Habit //TODO to Either

    suspend fun upsertHabit(habit: Habit): Either<DbException, Int>

    suspend fun deleteHabit(habit: Habit)

    suspend fun deleteAllHabits()

    suspend fun addHabitDone(habitDone: HabitDone): Int

    suspend fun deleteHabitDone(habitDoneId: Int)
}