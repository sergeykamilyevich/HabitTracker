package com.example.habittracker.db_api.domain.usecases

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError
import com.example.habittracker.core_api.domain.models.Habit

interface DeleteHabitUseCase {

    suspend operator fun invoke(habit: Habit): Either<IoError, Unit>
}