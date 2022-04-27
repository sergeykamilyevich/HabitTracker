package com.example.habittracker.domain.usecases.common

import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.repositories.SyncHabitRepository
import javax.inject.Inject

class SyncAllFromCloudUseCase @Inject constructor(
    private val syncHabitRepository: SyncHabitRepository
) {

    suspend operator fun invoke(): Either<IoError, Unit> = syncHabitRepository.syncAllFromCloud()
}