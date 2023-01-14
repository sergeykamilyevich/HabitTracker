package com.example.habittracker.cloud_sync.domain.usecases.impls

import com.example.habittracker.cloud_sync.domain.usecases.interfaces.*
import javax.inject.Inject

class SyncUseCaseImpl @Inject constructor(
    override val syncAllFromCloudUseCase: SyncAllFromCloudUseCase,
    override val syncAllToCloudUseCase: SyncAllToCloudUseCase,
    override val putHabitAndSyncWithDbUseCase: PutHabitAndSyncWithDbUseCase,
    override val areCloudAndDbEqualUseCase: AreCloudAndDbEqualUseCase
) : SyncUseCase