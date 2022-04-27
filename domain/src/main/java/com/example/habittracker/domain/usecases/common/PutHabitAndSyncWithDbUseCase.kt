package com.example.habittracker.domain.usecases.common

import com.example.habittracker.domain.models.CloudError
import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repositories.SyncHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PutHabitAndSyncWithDbUseCase @Inject constructor(
    private val syncHabitRepository: SyncHabitRepository,
) {

    suspend operator fun invoke(habit: Habit, newHabitId: Int): Either<CloudError, String> =
        syncHabitRepository.putAndSyncWithDb(habit, newHabitId)

}
