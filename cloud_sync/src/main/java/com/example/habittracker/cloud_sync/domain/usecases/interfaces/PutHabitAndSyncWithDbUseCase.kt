package com.example.habittracker.cloud_sync.domain.usecases.interfaces

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit

interface PutHabitAndSyncWithDbUseCase {

    suspend operator fun invoke(habit: Habit): Either<IoError, String>
}