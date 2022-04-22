package com.example.habittracker.domain.usecases.common

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncUseCase  @Inject constructor(
    val syncAllFromCloudUseCase: SyncAllFromCloudUseCase,
    val syncAllToCloudUseCase: SyncAllToCloudUseCase,
//    val uploadAllToCloudUseCase: UploadAllToCloudUseCase,
    val upsertAndPutHabitUseCase: UpsertAndPutHabitUseCase
)