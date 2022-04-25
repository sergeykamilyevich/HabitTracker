package com.example.habittracker.domain.repositories

import com.example.habittracker.domain.models.CloudError
import com.example.habittracker.domain.models.DbException
import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.Habit

interface SyncHabitRepository {

    suspend fun uploadAllToCloud(habitList: List<Habit>)

    suspend fun upsertAndSyncWithCloud(habit: Habit): Either<DbException, Int>

    suspend fun putAndSyncWithDb(habit: Habit, newHabitId: Int): Either<CloudError, String>

    suspend fun syncAllFromCloud()

    suspend fun syncAllToCloud()
}