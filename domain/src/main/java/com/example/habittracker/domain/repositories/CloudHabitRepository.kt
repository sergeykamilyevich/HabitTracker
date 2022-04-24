package com.example.habittracker.domain.repositories

import com.example.habittracker.domain.models.CloudResponseError
import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.models.HabitDone

interface CloudHabitRepository {

    suspend fun getHabitList(
    ): Either<CloudResponseError, List<Habit>>

    suspend fun putHabit(habit: Habit): Either<CloudResponseError, String>

    suspend fun deleteHabit(habit: Habit): Either<CloudResponseError, Unit>

    suspend fun postHabitDone(habitDone: HabitDone): Either<CloudResponseError, Unit>

    suspend fun deleteAllHabits()
}