package com.example.habittracker.domain.usecases.common

import com.example.habittracker.domain.repositories.SyncHabitRepository
import javax.inject.Inject

class SyncAllFromCloudUseCase @Inject constructor(
    private val syncHabitRepository: SyncHabitRepository
) {

    suspend operator fun invoke() {
        syncHabitRepository.syncAllFromCloud()
    }
}