package com.example.habittracker.domain.repositories

import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.models.*
import kotlinx.coroutines.flow.Flow

interface DbHabitRepository {

    fun getHabitList(
        habitType: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<Habit>>

    suspend fun getUnfilteredList(): List<Habit>? //TODO refactor to non-null

    suspend fun getHabitById(habitItemId: Int): Either<IoError, Habit>

    suspend fun upsertHabit(habit: Habit): Either<IoError, Int>

    suspend fun deleteHabit(habit: Habit)

    suspend fun deleteAllHabits()

    suspend fun addHabitDone(habitDone: HabitDone): Int

    suspend fun deleteHabitDone(habitDoneId: Int)
}