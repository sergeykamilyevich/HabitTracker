package com.example.habittracker.cloud_sync.domain.usecases.interfaces

interface SyncUseCase {

    val syncAllFromCloudUseCase: SyncAllFromCloudUseCase

    val syncAllToCloudUseCase: SyncAllToCloudUseCase

    val putHabitAndSyncWithDbUseCase: PutHabitAndSyncWithDbUseCase

    val areCloudAndDbEqualUseCase: AreCloudAndDbEqualUseCase
}