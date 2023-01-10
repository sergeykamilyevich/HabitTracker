package com.example.habittracker.feature_habits.domain.usecases.common

import javax.inject.Inject

//@Singleton
class SyncUseCase @Inject constructor(
    val syncAllFromCloudUseCase: SyncAllFromCloudUseCase,
    val syncAllToCloudUseCase: SyncAllToCloudUseCase,
    val putHabitAndSyncWithDbUseCase: PutHabitAndSyncWithDbUseCase,
    val areCloudAndDbEqualUseCase: AreCloudAndDbEqualUseCase
)