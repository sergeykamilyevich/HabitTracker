package com.example.habittracker.domain.repositories

import com.example.habittracker.domain.models.*

interface SyncHabitRepository {

    suspend fun uploadAllToCloud(habitList: List<HabitWithDone>)

    suspend fun upsertAndSyncWithCloud(habit: Habit): Either<DbException, Int>

    suspend fun putAndSyncWithDb(habit: Habit, newHabitId: Int): Either<CloudResponseError, String>

    suspend fun syncAllFromCloud()

    suspend fun syncAllToCloud()
}