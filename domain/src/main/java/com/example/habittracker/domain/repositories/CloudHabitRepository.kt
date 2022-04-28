package com.example.habittracker.domain.repositories

import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitDone

interface CloudHabitRepository {

    suspend fun getHabitList(
    ): Either<IoError, List<Habit>>

    suspend fun putHabit(habit: Habit): Either<IoError, String>

    suspend fun deleteHabit(habit: Habit): Either<IoError, Unit>

    suspend fun postHabitDone(habitDone: HabitDone): Either<IoError, Unit>

    suspend fun deleteAllHabits(): Either<IoError, Unit>
}