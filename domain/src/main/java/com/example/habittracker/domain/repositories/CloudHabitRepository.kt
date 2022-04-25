package com.example.habittracker.domain.repositories

import com.example.habittracker.domain.models.CloudError
import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitDone

interface CloudHabitRepository {

    suspend fun getHabitList(
    ): Either<CloudError, List<Habit>>

    suspend fun putHabit(habit: Habit): Either<CloudError, String>

    suspend fun deleteHabit(habit: Habit): Either<CloudError, Unit>

    suspend fun postHabitDone(habitDone: HabitDone): Either<CloudError, Unit>

    suspend fun deleteAllHabits()
}