package com.example.habittracker.cloud_sync.domain.usecases.interfaces

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import javax.inject.Inject

class SyncUseCase @Inject constructor(
    val areCloudAndDbEqualUseCase: AreCloudAndDbEqualUseCase,
    val syncAllToCloudUseCase: SyncAllToCloudUseCase,
    val putHabitAndSyncWithDbUseCase: PutHabitAndSyncWithDbUseCase,
    val syncAllFromCloudUseCase: SyncAllFromCloudUseCase,
)

fun interface PutHabitAndSyncWithDbUseCase : suspend (Habit) -> Either<IoError, String>

fun interface SyncAllFromCloudUseCase : suspend () -> Either<IoError, Unit>

fun interface UploadAllToCloudUseCase : suspend (List<Habit>) -> Either<IoError, Unit>