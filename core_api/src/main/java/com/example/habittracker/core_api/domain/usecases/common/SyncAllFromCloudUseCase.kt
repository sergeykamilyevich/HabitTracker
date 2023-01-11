package com.example.habittracker.core_api.domain.usecases.common

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.repositories.SyncHabitRepository
import javax.inject.Inject

class SyncAllFromCloudUseCase @Inject constructor(
    private val syncHabitRepository: SyncHabitRepository
) {

    suspend operator fun invoke(): Either<IoError, Unit> = syncHabitRepository.syncAllFromCloud()
}