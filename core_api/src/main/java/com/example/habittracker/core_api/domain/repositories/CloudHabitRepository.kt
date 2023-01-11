package com.example.habittracker.core_api.domain.repositories

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.models.HabitDone

interface CloudHabitRepository {

    suspend fun getHabitList(
    ): Either<IoError, List<Habit>>

    suspend fun putHabit(habit: Habit): Either<IoError, String>

    suspend fun deleteHabit(habit: Habit): Either<IoError, Unit>

    suspend fun postHabitDone(habitDone: HabitDone): Either<IoError, Unit>

    suspend fun deleteAllHabits(): Either<IoError, Unit>
}