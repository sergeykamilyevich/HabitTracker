package com.example.habittracker.db_api.domain.usecases

import com.example.habittracker.core_api.domain.errors.Either
import com.example.habittracker.core_api.domain.errors.IoError

interface DeleteAllHabitsUseCase {

    suspend operator fun invoke(): Either<IoError, Unit>
}