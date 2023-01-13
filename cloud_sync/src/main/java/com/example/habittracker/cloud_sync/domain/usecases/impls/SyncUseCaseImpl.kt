package com.example.habittracker.cloud_sync.domain.usecases.impls

import com.example.habittracker.cloud_sync.domain.usecases.interfaces.SyncUseCase
import javax.inject.Inject

class SyncUseCaseImpl @Inject constructor(
    override val syncAllFromCloudUseCase: SyncAllFromCloudUseCaseImpl,
    override val syncAllToCloudUseCase: SyncAllToCloudUseCaseImpl,
    override val putHabitAndSyncWithDbUseCase: PutHabitAndSyncWithDbUseCaseImpl,
    override val areCloudAndDbEqualUseCase: AreCloudAndDbEqualUseCaseImpl
) : SyncUseCase