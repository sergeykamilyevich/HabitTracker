package com.example.habittracker.cloud_sync.domain.usecases.impls

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.cloud_sync.domain.repositories.SyncHabitRepository
import com.example.habittracker.cloud_sync.domain.usecases.interfaces.PutHabitAndSyncWithDbUseCase
import javax.inject.Inject

class PutHabitAndSyncWithDbUseCaseImpl @Inject constructor(
    private val syncHabitRepository: SyncHabitRepository,
) : PutHabitAndSyncWithDbUseCase {

    override suspend operator fun invoke(habit: Habit): Either<IoError, String> =
        syncHabitRepository.putAndSyncWithDb(habit)

}
