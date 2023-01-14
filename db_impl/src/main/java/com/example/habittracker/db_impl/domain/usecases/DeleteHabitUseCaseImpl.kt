package com.example.habittracker.db_impl.domain.usecases

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit
import com.example.habittracker.core_api.domain.repositories.DbHabitRepository
import com.example.habittracker.db_api.domain.usecases.DeleteHabitUseCase
import javax.inject.Inject

class DeleteHabitUseCaseImpl @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) : DeleteHabitUseCase {

    override suspend operator fun invoke(habit: Habit): Either<IoError, Unit> =
        dbHabitRepository.deleteHabit(habit)
}