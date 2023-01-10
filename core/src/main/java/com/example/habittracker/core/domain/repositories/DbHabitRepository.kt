package com.example.habittracker.core.domain.repositories

import com.example.habittracker.core.domain.errors.Either
import com.example.habittracker.core.domain.errors.IoError
import com.example.habittracker.core.domain.models.Habit
import com.example.habittracker.core.domain.models.HabitDone
import com.example.habittracker.core.domain.models.HabitListFilter
import com.example.habittracker.core.domain.models.HabitType
import kotlinx.coroutines.flow.Flow

interface DbHabitRepository {

    fun getHabitList(
        habitType: HabitType?,
        habitListFilter: HabitListFilter
    ): Flow<List<Habit>>

    suspend fun getUnfilteredList(): Either<IoError, List<Habit>>

    suspend fun getHabitById(habitId: Int): Either<IoError, Habit>

    suspend fun upsertHabit(habit: Habit): Either<IoError, Int>

    suspend fun deleteHabit(habit: Habit): Either<IoError, Unit>

    suspend fun deleteAllHabits(): Either<IoError, Unit>

    suspend fun addHabitDone(habitDone: HabitDone): Either<IoError, Int>

    suspend fun deleteHabitDone(habitDoneId: Int)
}