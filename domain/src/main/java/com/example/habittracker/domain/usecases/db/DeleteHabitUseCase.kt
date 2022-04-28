package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.errors.Either
import com.example.habittracker.domain.errors.IoError
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repositories.DbHabitRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) {

    suspend operator fun invoke(habit: Habit): Either<IoError, Unit> =
        dbHabitRepository.deleteHabit(habit)
}