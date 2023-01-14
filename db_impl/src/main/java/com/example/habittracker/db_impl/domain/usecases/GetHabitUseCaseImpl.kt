package com.example.habittracker.db_impl.domain.usecases

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import com.example.habittracker.db_api.domain.usecases.GetHabitUseCase
import javax.inject.Inject

class GetHabitUseCaseImpl @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) : GetHabitUseCase {

    override suspend operator fun invoke(habitItemId: Int): Either<IoError, Habit> =
        dbHabitRepository.getHabitById(habitItemId)
}