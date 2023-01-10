package com.example.habittracker.feature_habits.domain.usecases.common

import com.example.habittracker.core.domain.errors.Either
import com.example.habittracker.core.domain.errors.IoError
import com.example.habittracker.core.domain.models.Habit
import com.example.habittracker.core.domain.repositories.SyncHabitRepository
import javax.inject.Inject

//@Singleton
class PutHabitAndSyncWithDbUseCase @Inject constructor(
    private val syncHabitRepository: SyncHabitRepository,
) {

    suspend operator fun invoke(habit: Habit): Either<IoError, String> =
        syncHabitRepository.putAndSyncWithDb(habit)

}
