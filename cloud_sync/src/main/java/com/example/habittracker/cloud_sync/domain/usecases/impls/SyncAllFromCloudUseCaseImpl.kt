package com.example.habittracker.cloud_sync.domain.usecases.impls

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.cloud_sync.domain.repositories.SyncHabitRepository
import com.example.habittracker.cloud_sync.domain.usecases.interfaces.SyncAllFromCloudUseCase
import javax.inject.Inject

class SyncAllFromCloudUseCaseImpl @Inject constructor(
    private val syncHabitRepository: SyncHabitRepository
) : SyncAllFromCloudUseCase {

    override suspend operator fun invoke(): Either<IoError, Unit> = syncHabitRepository.syncAllFromCloud()
}