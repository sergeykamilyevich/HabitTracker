package com.example.habittracker.domain.usecases.db

import com.example.habittracker.domain.models.HabitDone
import com.example.habittracker.domain.repositories.DbHabitRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddHabitDoneToDbUseCase @Inject constructor(
    private val dbHabitRepository: DbHabitRepository
) { //TODO add dispatcher

    suspend operator fun invoke(habitDone: HabitDone): Int {
        return dbHabitRepository.addHabitDone(habitDone)
    }
}