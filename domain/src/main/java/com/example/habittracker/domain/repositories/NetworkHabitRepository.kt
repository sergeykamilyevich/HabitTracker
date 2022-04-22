package com.example.habittracker.domain.repositories

import com.example.habittracker.domain.models.*

interface NetworkHabitRepository {

    suspend fun getHabitList(
    ): List<Habit>? //TODO is nullable good idea?

    suspend fun putHabit(habit: Habit): String?

    suspend fun deleteHabit(habit: Habit): String?

    suspend fun postHabitDone(habitDone: HabitDone): String?

    suspend fun deleteAllHabits()
}