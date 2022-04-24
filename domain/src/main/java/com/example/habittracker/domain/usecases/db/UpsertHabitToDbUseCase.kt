package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.models.DbException
import com.example.habittracker.domain.models.Either
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.domain.repositories.DbHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpsertHabitToDbUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository,
) {

    suspend operator fun invoke(habit: Habit): Either<DbException, Int> =
        dbHabitRepository.upsertHabit(habit)
}