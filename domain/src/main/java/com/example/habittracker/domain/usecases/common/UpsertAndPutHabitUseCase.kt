package com.example.habittracker.domain.usecases.common

import com.example.habittracker.domain.models.DbException
import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repositories.SyncHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpsertAndPutHabitUseCase @Inject constructor(
    private val syncHabitRepository: SyncHabitRepository,
) {

    suspend operator fun invoke(habit: Habit): Either<DbException, Int> {
        val resultOfUpserting = syncHabitRepository.upsertAndSyncWithCloud(habit)
        return resultOfUpserting
    }
}
