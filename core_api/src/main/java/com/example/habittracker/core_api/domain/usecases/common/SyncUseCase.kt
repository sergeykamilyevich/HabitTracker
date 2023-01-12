package com.example.habittracker.core_api.domain.usecases.common

import javax.inject.Inject

class SyncUseCase @Inject constructor(
    val syncAllFromCloudUseCase: SyncAllFromCloudUseCase,
    val syncAllToCloudUseCase: SyncAllToCloudUseCase,
    val putHabitAndSyncWithDbUseCase: PutHabitAndSyncWithDbUseCase,
    val areCloudAndDbEqualUseCase: AreCloudAndDbEqualUseCase
)