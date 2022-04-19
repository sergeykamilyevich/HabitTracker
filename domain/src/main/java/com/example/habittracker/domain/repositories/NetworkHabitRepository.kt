package com.example.habittracker.domain.repositories

import com.example.habittracker.domain.models.*
import kotlinx.coroutines.flow.Flow

interface NetworkHabitRepository {

    suspend fun getHabitList(
    ): List<HabitItem>? //TODO is nullable good idea?

    suspend fun putHabit(habitItem: HabitItem)

    suspend fun deleteHabit(habitItem: HabitItem)

    suspend fun postHabitDone(habitDone: HabitDone)
}